package com.ktor.api.hirebeat.modules.gig_requests.infrastructure.rest

import com.ktor.api.hirebeat.modules.gig_requests.application.*
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.RequestStatus
import com.ktor.api.hirebeat.modules.gig_requests.infrastructure.rest.dto.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.gigRequestRouting() {
    val createUseCase by inject<CreateGigRequestUseCase>()
    val updateStatusUseCase by inject<UpdateGigRequestStatusUseCase>()
    val getMusicianRequestsUseCase by inject<GetMusicianGigRequestsUseCase>()
    val getRecruiterRequestsUseCase by inject<GetRecruiterGigRequestsUseCase>()

    route("/requests") {
        authenticate("auth-jwt") {

            post {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val recruiterIdStr = principal?.payload?.getClaim("id")?.asString()
                        ?: principal?.payload?.getClaim("userId")?.asString()
                    val recruiterId = UUID.fromString(recruiterIdStr ?: throw IllegalArgumentException("Token inválido"))

                    val dto = call.receive<CreateGigRequestDto>()

                    val errors = dto.getErrors()
                    if (errors.isNotEmpty()) {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("errors" to errors))
                    }

                    val request = createUseCase.execute(dto.toDomain(), recruiterId)

                    call.respond(
                        HttpStatusCode.Created,
                        CreateGigRequestResponseDto(id = request.id.toString(), message = "Solicitud enviada")
                    )

                } catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.Conflict, mapOf("error" to (e.message ?: "Músico ocupado")))
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Datos inválidos")))
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error en el servidor: ${e.message}"))
                }
            }

            put("/{id}/status") {
                try {
                    val requestId = UUID.fromString(call.parameters["id"])
                    val dto = call.receive<UpdateStatusDto>()

                    val status = when (dto.status.uppercase()) {
                        "ACCEPTED" -> RequestStatus.ACEPTADA
                        "REJECTED" -> RequestStatus.RECHAZADA
                        "PENDING" -> RequestStatus.PENDIENTE
                        "CANCELED" -> RequestStatus.CANCELADA
                        "COMPLETED" -> RequestStatus.COMPLETADA
                        else -> RequestStatus.valueOf(dto.status.uppercase())
                    }

                    updateStatusUseCase.execute(requestId, status)
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Estado actualizado"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error procesando solicitud")))
                }
            }

            get("/musician") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userIdStr = principal?.payload?.getClaim("id")?.asString()
                        ?: principal?.payload?.getClaim("userId")?.asString()
                    val userId = UUID.fromString(userIdStr ?: throw IllegalArgumentException("Token inválido"))

                    val requests = getMusicianRequestsUseCase.execute(userId)
                    call.respond(HttpStatusCode.OK, requests.map { it.toResponse() })
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Perfil no encontrado")))
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error: ${e.message}"))
                }
            }

            get("/recruiter") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val recruiterIdStr = principal?.payload?.getClaim("id")?.asString()
                        ?: principal?.payload?.getClaim("userId")?.asString()
                    val recruiterId = UUID.fromString(recruiterIdStr ?: throw IllegalArgumentException("Token inválido"))

                    val requests = getRecruiterRequestsUseCase.execute(recruiterId)
                    call.respond(HttpStatusCode.OK, requests.map { it.toResponse() })
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error: ${e.message}"))
                }
            }
        }
    }
}
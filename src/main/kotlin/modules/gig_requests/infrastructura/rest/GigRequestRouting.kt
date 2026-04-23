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
                    val recruiterId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                    val dto = call.receive<CreateGigRequestDto>()
                    val request = createUseCase.execute(dto.toDomain(recruiterId))

                    call.respond(HttpStatusCode.Created, request.id.toString())
                } catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.Conflict, e.message ?: "Músico ocupado")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error en el servidor")
                }
            }

            put("/{id}/status") {
                try {
                    val requestId = UUID.fromString(call.parameters["id"])
                    val dto = call.receive<UpdateStatusDto>()
                    val status = RequestStatus.valueOf(dto.status.uppercase())

                    updateStatusUseCase.execute(requestId, status)
                    call.respond(HttpStatusCode.OK, "Estado actualizado")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Error procesando solicitud")
                }
            }

            get("/musician") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                    val requests = getMusicianRequestsUseCase.execute(userId)
                    call.respond(HttpStatusCode.OK, requests.map { it.toResponse() })
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "Perfil no encontrado")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error en el servidor")
                }
            }

            get("/recruiter") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val recruiterId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                    val requests = getRecruiterRequestsUseCase.execute(recruiterId)
                    call.respond(HttpStatusCode.OK, requests.map { it.toResponse() })
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error en el servidor")
                }
            }
        }
    }
}
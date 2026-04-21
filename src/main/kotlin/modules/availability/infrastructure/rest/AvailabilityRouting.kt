package com.ktor.api.hirebeat.modules.availability.infrastructure.rest

import com.ktor.api.hirebeat.modules.availability.application.*
import com.ktor.api.hirebeat.modules.availability.infrastructure.rest.dto.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.util.UUID

fun Route.availabilityRouting() {
    val addBlockedSlotUseCase by inject<AddBlockedSlotUseCase>()
    val getAvailabilityUseCase by inject<GetProfileAvailabilityUseCase>()

    route("/availability") {

        authenticate("auth-jwt") {
            post {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                    val request = call.receive<CreateBlockedSlotRequest>()
                    val errors = request.getErrors()
                    if (errors.isNotEmpty()) return@post call.respond(HttpStatusCode.BadRequest, errors)

                    val slot = addBlockedSlotUseCase.execute(userId, request.toDomain())
                    call.respond(HttpStatusCode.Created, slot.toResponse())
                } catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.Conflict, e.message ?: "Cruce de horarios")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error en el servidor")
                }
            }

            delete("/{id}") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())
                    val slotId = UUID.fromString(call.parameters["id"])

                    val deleteBlockedSlotUseCase by inject<DeleteBlockedSlotUseCase>()
                    deleteBlockedSlotUseCase.execute(userId, slotId)

                    call.respond(HttpStatusCode.NoContent)
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "Horario no encontrado")
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Formato de ID inválido")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error en el servidor")
                }
            }
        }

        get("/profile/{profileId}") {
            try {
                val profileId = UUID.fromString(call.parameters["profileId"])

                val startParam = call.request.queryParameters["start"]
                val endParam = call.request.queryParameters["end"]

                val start = startParam?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
                val end = endParam?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now().plusMonths(1)

                val slots = getAvailabilityUseCase.execute(profileId, start, end)
                call.respond(HttpStatusCode.OK, slots.map { it.toResponse() })
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos en el request")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error obteniendo calendario")
            }
        }
    }
}
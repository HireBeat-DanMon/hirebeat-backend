package com.ktor.api.hirebeat.modules.profile.infrastructure.rest

import com.ktor.api.hirebeat.modules.profile.application.*
import com.ktor.api.hirebeat.modules.profile.infrastructure.rest.dto.*
import com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.profileRouting() {
    val updateUseCase by inject<UpdateProfileUseCase>()
    val getByUserIdUseCase by inject<GetByIdProfileUseCase>()
    val getMyProfile by inject<GetMyProfile>()
    val getAllProfileUseCase by inject <GetAllProfileUseCase>()

    route("/profile") {

        // Todo lo que requiera Token debe ir dentro de este bloque
        authenticate("auth-jwt") {
            put {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val claimId = principal?.payload?.getClaim("id")?.asString()

                    val userId = claimId?.let { UUID.fromString(it) }
                        ?: throw IllegalArgumentException("El claim 'id' no existe o no es un UUID válido: $claimId")

                    val request = call.receive<ProfileRequest>()
                    val errors = request.getErrors()
                    if (errors.isNotEmpty()) {
                        return@put call.respond(HttpStatusCode.BadRequest, errors)
                    }

                    val profile = updateUseCase.execute(userId, request.toDomain(userId = userId))
                    call.respond(HttpStatusCode.OK, profile.toResponse())
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "Profile not found")
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
                } catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.Conflict, e.message ?: "Server Error")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error interno del servidor")
                }
            }

            get("/me") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val claimId = principal?.payload?.getClaim("id")?.asString()

                    val userId = claimId?.let { UUID.fromString(it) }
                        ?: throw IllegalArgumentException("El claim 'id' no existe o no es un UUID válido: $claimId")

                    val profile = getMyProfile.execute(userId)
                    call.respond(HttpStatusCode.OK, profile.toResponse())
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "Profile not found")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error interno del servidor")
                }
            }

            // Moviendo POST /image AQUÍ ADENTRO para que esté protegido por el token
            post("/image") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val claimId = principal?.payload?.getClaim("id")?.asString()
                    val userId = claimId?.let { UUID.fromString(it) } ?: throw IllegalArgumentException("Token inválido")

                    val multipartData = call.receiveMultipart()
                    var fileBytes: ByteArray? = null
                    var originalFileName = "image.jpg"

                    multipartData.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                originalFileName = part.originalFileName as String
                                fileBytes = part.streamProvider().readBytes()
                            }
                            else -> {}
                        }
                        part.dispose()
                    }

                    if (fileBytes != null) {
                        val uploadUseCase by inject<UploadProfileImageUseCase>()
                        val imageUrl = uploadUseCase.execute(userId, fileBytes!!, originalFileName)
                        call.respond(HttpStatusCode.OK, mapOf("url" to imageUrl))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "No se adjuntó ninguna imagen")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error subiendo la imagen")
                }
            }
        } // Fin del bloque authenticate

        // Estas rutas son públicas (no requieren token)
        get("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"]) ?: throw IllegalArgumentException("Invalid UUID format")
                val profile = getByUserIdUseCase.execute(id)
                call.respond(HttpStatusCode.OK, profile.toResponse())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message ?: "Profile not found")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error interno del servidor")
            }
        }

        get(){
            val profiles = getAllProfileUseCase.execute()
            val response = profiles?.map { it.toResponse() } ?: emptyList()
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
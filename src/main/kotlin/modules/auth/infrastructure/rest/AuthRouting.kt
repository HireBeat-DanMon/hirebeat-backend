package com.ktor.api.hirebeat.modules.auth.infrastructure.rest

import com.ktor.api.hirebeat.modules.auth.application.*
import com.ktor.api.hirebeat.modules.auth.infrastructure.rest.dto.*
import com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.authRouting() {
    val loginUseCase by inject<LoginUseCase>()
    val registerUseCase by inject<RegisterUseCase>()

    route("/auth") {

        post("/login") {
            try {
                val request = call.receive<LoginRequest>()

                val errors = request.getErrors()
                if (errors.isNotEmpty()) {
                    return@post call.respond(HttpStatusCode.BadRequest, errors)
                }

                // Desestructuramos el resultado del UseCase para obtener ambos valores
                val (token, roleId) = loginUseCase.execute(request.email, request.password)

                // Enviamos ambos en el JSON de respuesta
                call.respond(HttpStatusCode.OK, mapOf(
                    "token" to token,
                    "roleId" to (roleId ?: "")
                ))
            } catch (e : SecurityException){
                call.respond(HttpStatusCode.Unauthorized, e.message ?: "Unauthorized")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Server Problem")
            }
        }

        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                println(UUID.randomUUID())
                val errors = request.getErrors()
                if (errors.isNotEmpty()) {
                    return@post call.respond(HttpStatusCode.BadRequest, errors)
                }
                val createdUser = registerUseCase.execute(request.toDomain())
                call.respond(HttpStatusCode.Created, createdUser.toResponse())
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Conflict occurred")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message?:"Server Problem")
            }
        }
    }
}
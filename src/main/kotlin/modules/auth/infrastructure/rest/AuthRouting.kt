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

                val (token, roleId) = loginUseCase.execute(request.email, request.password)

                call.respond(HttpStatusCode.OK, mapOf(
                    "token" to token,
                    "roleId" to (roleId ?: "")
                ))
            } catch (e : SecurityException){
                call.respond(HttpStatusCode.Unauthorized, mapOf("Error" to (e.message ?: "Unauthorized")))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("Error" to (e.message ?: "Invalid Data")))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("Error" to ("Server Problem")))
            }
        }

        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                val errors = request.getErrors()
                if (errors.isNotEmpty()) {
                    return@post call.respond(HttpStatusCode.BadRequest, errors)
                }
                val createdUser = registerUseCase.execute(request.toDomain())
                call.respond(HttpStatusCode.Created, createdUser.toResponse())
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, mapOf("Error" to (e.message ?: "Conflict occurred")))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("Error" to (e.message ?: "Invalid Data")))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("Error" to (e.message?:"Server Problem")))
            }
        }
    }
}
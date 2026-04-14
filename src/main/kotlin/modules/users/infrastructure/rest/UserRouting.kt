package com.ktor.api.hirebeat.modules.users.infrastructure.rest

import com.ktor.api.hirebeat.modules.users.application.*
import com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.userRouting() {
    val getUserUseCase by inject<GetByIdUserUseCase>()
    val getAllUseCase by inject<GetAllUserUseCase>()
    val updateUseCase by inject<UpdateUserUseCase>()
    val deleteUserUseCase by inject<DeleteUserUseCase>()

    route("/user") {

        route("/{id}") {

            get {
                try {
                    val id = UUID.fromString(call.parameters["id"]) ?: throw IllegalArgumentException("Invalid UUID format")
                    val user = getUserUseCase.execute(id)
                    call.respond(HttpStatusCode.OK, user.toResponse())
                }catch (e : NotFoundException){
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found")
                }catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
                }
                catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Server Problem")
                }
            }

            put {
                try {
                    val id = UUID.fromString(call.parameters["id"]) ?: throw IllegalArgumentException("Invalid UUID format")
                    val request = call.receive<UpdateUserRequest>()

                    val errors = request.getErrors()
                    if (errors.isNotEmpty()) {
                        return@put call.respond(HttpStatusCode.BadRequest, errors)
                    }

                    val update = updateUseCase.execute(id, request.toDomain(),request.passwordOld)
                    call.respond(HttpStatusCode.OK, update.toResponse())
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found .")
                }catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
                }catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, e.message ?: "Server Problem")
                }
            }

            delete {
                try {
                    val id = UUID.fromString(call.parameters["id"])
                    deleteUserUseCase.execute(id)
                    call.respond(HttpStatusCode.NoContent)
                }catch (e : NotFoundException){
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found")
                }catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid Data")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Server Problem")
                }
            }
        }

        get(){
            val users = getAllUseCase.execute()
            val response = users?.map { it.toResponse() } ?: emptyList()
            call.respond(HttpStatusCode.OK, response)
        }

    }
}
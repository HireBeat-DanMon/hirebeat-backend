package com.ktor.api.hirebeat.modules.reviews.infrastructure.rest

import com.ktor.api.hirebeat.modules.reviews.application.*
import com.ktor.api.hirebeat.modules.reviews.infrastructure.rest.dto.CreateReviewRequest
import com.ktor.api.hirebeat.modules.reviews.infrastructure.rest.dto.toResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.reviewRouting() {
    val createReviewUseCase by inject<CreateReviewUseCase>()
    val getProfileReviewsUseCase by inject<GetProfileReviewsUseCase>()
    val getMyReviewsUseCase by inject<GetMyReviewsUseCase>()

    route("/reviews") {
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val reviewerId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                val request = call.receive<CreateReviewRequest>()
                val review = createReviewUseCase.execute(
                    reviewerId = reviewerId,
                    musicianProfileId = UUID.fromString(request.musicianProfileId),
                    rating = request.rating,
                    comment = request.comment
                )
                call.respond(HttpStatusCode.Created, mapOf("id" to review.id.toString(), "message" to "Reseña creada"))
            }

            get("/me") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = UUID.fromString(principal?.payload?.getClaim("id")?.asString())

                    val reviews = getMyReviewsUseCase.execute(userId)
                    call.respond(HttpStatusCode.OK, reviews.map { it.toResponse() })
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al obtener tus reseñas")
                }
            }
        }

        get("/profile/{profileId}") {
            try {
                val profileIdParam = call.parameters["profileId"]
                    ?: throw IllegalArgumentException("Se requiere el ID del perfil")
                val profileId = UUID.fromString(profileIdParam)

                val reviews = getProfileReviewsUseCase.execute(profileId)
                call.respond(HttpStatusCode.OK, reviews.map { it.toResponse() })
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "ID inválido")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al obtener reseñas")
            }
        }
    }
}
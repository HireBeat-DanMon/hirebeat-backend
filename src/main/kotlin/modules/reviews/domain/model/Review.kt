package com.ktor.api.hirebeat.modules.reviews.domain.model

import com.ktor.api.hirebeat.modules.users.domain.model.User
import java.time.LocalDateTime
import java.util.UUID

data class Review(
    val id: UUID? = null,
    val reviewer: User? = null,
    val musicianProfileId: UUID,
    val rating: Int, // 1 al 5
    val comment: String,
    val createdAt: LocalDateTime? = null
) {
    init {
        require(rating in 1..5) { "La calificación debe estar entre 1 y 5" }
        require(comment.isNotBlank()) { "El comentario no puede estar vacío" }
    }
}
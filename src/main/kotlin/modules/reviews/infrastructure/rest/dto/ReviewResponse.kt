package com.ktor.api.hirebeat.modules.reviews.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val createdAt: String
)

fun Review.toResponse() = ReviewResponse(
    id = id ?: UUID.randomUUID(),
    reviewerName = reviewer?.fullname ?: "Usuario Anónimo",
    rating = rating,
    comment = comment,
    createdAt = createdAt?.toString() ?: ""
)
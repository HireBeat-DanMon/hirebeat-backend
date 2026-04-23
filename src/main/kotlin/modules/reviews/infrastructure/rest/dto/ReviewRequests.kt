package com.ktor.api.hirebeat.modules.reviews.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewRequest(
    val musicianProfileId: String,
    val rating: Int,
    val comment: String
)
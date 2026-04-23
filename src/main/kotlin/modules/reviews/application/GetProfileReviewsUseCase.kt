package com.ktor.api.hirebeat.modules.reviews.application

import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import java.util.UUID

class GetProfileReviewsUseCase(
    private val reviewRepository: ReviewRepository
) {
    suspend fun execute(profileId: UUID): List<Review> {
        return reviewRepository.findByMusicianProfileId(profileId)
    }
}
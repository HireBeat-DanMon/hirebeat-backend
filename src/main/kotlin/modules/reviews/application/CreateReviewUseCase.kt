package com.ktor.api.hirebeat.modules.reviews.application

import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import com.ktor.api.hirebeat.modules.users.domain.model.User
import java.util.UUID

class CreateReviewUseCase(
    private val reviewRepository: ReviewRepository
) {
    suspend fun execute(reviewerId: UUID, musicianProfileId: UUID, rating: Int, comment: String): Review {
        val review = Review(
            reviewer = User(id = reviewerId, email = "vacio@gmail.com", password = ""),
            musicianProfileId = musicianProfileId,
            rating = rating,
            comment = comment
        )
        return reviewRepository.save(review)
    }
}
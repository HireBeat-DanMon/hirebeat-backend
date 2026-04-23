package com.ktor.api.hirebeat.modules.reviews.application

import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class GetMyReviewsUseCase(
    private val reviewRepository: ReviewRepository,
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(userId: UUID): List<Review> {

        val profileId = newSuspendedTransaction {
            profileRepository.getIdByUserId(userId)
                ?: throw NotFoundException("No se encontró el perfil para el usuario $userId")
        }

        return reviewRepository.findByMusicianProfileId(profileId)
    }
}
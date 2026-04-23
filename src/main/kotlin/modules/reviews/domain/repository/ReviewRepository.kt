package com.ktor.api.hirebeat.modules.reviews.domain.repository

import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import java.util.UUID

interface ReviewRepository {
    suspend fun save(review: Review): Review
    suspend fun findByMusicianProfileId(profileId: UUID): List<Review>
    suspend fun findAll(): List<Review>
    suspend fun delete(id: UUID, reviewerId: UUID): Boolean
}
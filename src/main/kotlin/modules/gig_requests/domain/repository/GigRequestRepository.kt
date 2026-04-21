package com.ktor.api.hirebeat.modules.gig_requests.domain.repository

import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.RequestStatus
import java.util.UUID

interface GigRequestRepository {
    suspend fun save(request: GigRequest): GigRequest
    suspend fun findById(id: UUID): GigRequest?
    suspend fun findByMusicianProfileId(profileId: UUID): List<GigRequest>
    suspend fun findByRecruiterId(recruiterId: UUID): List<GigRequest>
    suspend fun updateStatus(id: UUID, status: RequestStatus): Boolean
}
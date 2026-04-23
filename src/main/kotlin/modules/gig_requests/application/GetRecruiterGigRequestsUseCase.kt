package com.ktor.api.hirebeat.modules.gig_requests.application

import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import java.util.UUID

class GetRecruiterGigRequestsUseCase(
    private val gigRequestRepository: GigRequestRepository
) {
    suspend fun execute(recruiterId: UUID): List<GigRequest> {
        return gigRequestRepository.findByRecruiterId(recruiterId)
    }
}
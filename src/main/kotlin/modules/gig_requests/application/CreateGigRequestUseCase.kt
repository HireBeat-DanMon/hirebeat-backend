package com.ktor.api.hirebeat.modules.gig_requests.application

import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository

class CreateGigRequestUseCase(
    private val gigRequestRepository: GigRequestRepository,
    private val blockedSlotRepository: BlockedSlotRepository
) {
    suspend fun execute(request: GigRequest): GigRequest {
        val isBusy = blockedSlotRepository.hasOverlap(request.musicianProfileId, request.startTime, request.endTime)
        if (isBusy) {
            throw IllegalStateException("El músico ya tiene un evento o bloqueo en este horario.")
        }

        return gigRequestRepository.save(request)
    }
}
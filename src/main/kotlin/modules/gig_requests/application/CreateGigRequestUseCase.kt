package com.ktor.api.hirebeat.modules.gig_requests.application

import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import java.util.UUID

class CreateGigRequestUseCase(
    private val gigRequestRepository: GigRequestRepository,
    private val blockedSlotRepository: BlockedSlotRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(request: GigRequest, recruiterId: UUID): GigRequest {
        val user = userRepository.findById(recruiterId)
            ?: throw IllegalArgumentException("Reclutador no encontrado")

        val requestWithUser = request.copy(recruiter = user)

        val isBusy = blockedSlotRepository.hasOverlap(requestWithUser.musicianProfileId, requestWithUser.startTime, requestWithUser.endTime)
        if (isBusy) {
            throw IllegalStateException("El músico ya tiene un evento o bloqueo en este horario.")
        }

        return gigRequestRepository.save(requestWithUser)
    }
}
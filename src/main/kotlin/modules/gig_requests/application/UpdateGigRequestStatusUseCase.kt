package com.ktor.api.hirebeat.modules.gig_requests.application

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.RequestStatus
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class UpdateGigRequestStatusUseCase(
    private val gigRequestRepository: GigRequestRepository,
    private val blockedSlotRepository: BlockedSlotRepository
) {
    suspend fun execute(requestId: UUID, newStatus: RequestStatus) {
        val request = gigRequestRepository.findById(requestId)
            ?: throw NotFoundException("Solicitud no encontrada")

        if (newStatus == RequestStatus.ACEPTADA && request.status != RequestStatus.ACEPTADA) {
            val isBusy = blockedSlotRepository.hasOverlap(request.musicianProfileId, request.startTime, request.endTime)
            if (isBusy) throw IllegalStateException("Ya no tienes disponibilidad para aceptar este evento.")

            blockedSlotRepository.save(
                BlockedSlot(
                    musicianProfileId = request.musicianProfileId,
                    startTime = request.startTime,
                    endTime = request.endTime,
                    reason = "Evento: ${request.messageDetails?.take(20)}...",
                    isAutomatic = true
                )
            )
        }

        gigRequestRepository.updateStatus(requestId, newStatus)
    }
}
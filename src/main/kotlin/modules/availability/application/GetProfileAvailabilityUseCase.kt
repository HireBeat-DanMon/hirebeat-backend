package com.ktor.api.hirebeat.modules.availability.application

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import java.time.LocalDateTime
import java.util.UUID

class GetProfileAvailabilityUseCase(
    private val blockedSlotRepository: BlockedSlotRepository
) {
    suspend fun execute(profileId: UUID, start: LocalDateTime, end: LocalDateTime): List<BlockedSlot> {
        return blockedSlotRepository.findByProfileIdAndDateRange(profileId, start, end)
    }
}
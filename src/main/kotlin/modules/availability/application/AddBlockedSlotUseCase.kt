package com.ktor.api.hirebeat.modules.availability.application

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class AddBlockedSlotUseCase(
    private val blockedSlotRepository: BlockedSlotRepository,
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(userId: UUID, slot: BlockedSlot): BlockedSlot {
        val profileId = profileRepository.getIdByUserId(userId)
            ?: throw NotFoundException("No se encontró el perfil de músico asociado a este usuario.")

        val slotWithProfile = slot.copy(musicianProfileId = profileId)

        if (blockedSlotRepository.hasOverlap(profileId, slotWithProfile.startTime, slotWithProfile.endTime)) {
            throw IllegalStateException("El horario seleccionado choca con otro evento o bloqueo existente.")
        }

        return blockedSlotRepository.save(slotWithProfile)
    }
}
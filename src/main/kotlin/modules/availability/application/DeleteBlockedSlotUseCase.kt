package com.ktor.api.hirebeat.modules.availability.application

import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class DeleteBlockedSlotUseCase(
    private val blockedSlotRepository: BlockedSlotRepository,
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(userId: UUID, slotId: UUID) {
        val profileId = profileRepository.getIdByUserId(userId)
            ?: throw NotFoundException("No se encontró el perfil de músico asociado a este usuario.")

        val isDeleted = blockedSlotRepository.delete(slotId, profileId)

        if (!isDeleted) {
            throw NotFoundException("El bloque de horario no existe o no te pertenece.")
        }
    }
}
package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend fun execute(userId: UUID, profile: Profile): Profile = newSuspendedTransaction {
        val profileId = repository.getIdByUserId(userId)
            ?: throw NotFoundException("No se encontró el perfil para el usuario $userId")

        val profileToUpdate = profile.copy(id = profileId)

        val isUpdated = repository.update(profileToUpdate)

        if (!isUpdated) {
            throw IllegalStateException("No se pudo actualizar el perfil en la base de datos")
        }

        repository.findById(profileId)
            ?: throw NotFoundException("Error al recuperar el perfil tras actualizar")
    }
}


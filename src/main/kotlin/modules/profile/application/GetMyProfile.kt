package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class GetMyProfile (
    private val repository: ProfileRepository
){
    suspend fun execute(userId: UUID) : Profile = newSuspendedTransaction {
        val profileId = repository.getIdByUserId(userId)
            ?: throw NotFoundException("No se encontró el perfil para el usuario $userId")

        repository.findById(profileId) ?: throw NotFoundException("The ID $id not found")
    }
}
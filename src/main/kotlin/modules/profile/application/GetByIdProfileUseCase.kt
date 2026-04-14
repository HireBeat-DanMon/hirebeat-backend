package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class GetByIdProfileUseCase(
    private val repository: ProfileRepository
){
    suspend fun execute(id: UUID) : Profile = newSuspendedTransaction {
        repository.findById(id) ?: throw NotFoundException("The ID $id not found")
    }
}
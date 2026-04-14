package com.ktor.api.hirebeat.modules.users.application

import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class DeleteUserUseCase(private val repository: UserRepository){
    suspend fun execute(id: UUID) {
        val deleted = repository.delete(id)
        if (!deleted) throw NotFoundException("The ID $id not found")
    }
}

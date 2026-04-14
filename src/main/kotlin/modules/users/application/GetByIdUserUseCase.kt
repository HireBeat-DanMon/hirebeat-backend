package com.ktor.api.hirebeat.modules.users.application

import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class GetByIdUserUseCase (private val repository: UserRepository){
    suspend fun execute (id : UUID) : User {
        return repository.findById(id)
            ?: throw NotFoundException ("The ID $id not found")
    }
}
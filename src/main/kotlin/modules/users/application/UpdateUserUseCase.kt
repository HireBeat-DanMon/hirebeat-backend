package com.ktor.api.hirebeat.modules.users.application

import com.ktor.api.hirebeat.common.infrastructure.security.PasswordHasher
import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class UpdateUserUseCase(
    private val repository: UserRepository,
    private val hasher: PasswordHasher
){
    suspend fun execute(id: UUID, user: User, passwordOld: String): User {
        val existingUser = repository.findById(id)
            ?: throw NotFoundException("User with ID $id not found")

        if (!hasher.check(passwordOld, existingUser.password)) {
            throw IllegalArgumentException("Invalid current password")
        }

        val userToUpdate = user.copy(
            id = id,
            password = hasher.hash(user.password)
        )

        return repository.update(userToUpdate)
            ?: throw NotFoundException("Failed to update user with ID $id")
    }
}

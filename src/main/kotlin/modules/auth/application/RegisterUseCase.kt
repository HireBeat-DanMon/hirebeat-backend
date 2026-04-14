package com.ktor.api.hirebeat.modules.auth.application

import com.ktor.api.hirebeat.common.infrastructure.security.PasswordHasher
import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val catalogRepository: CatalogRepository,
    private val profileRepository: ProfileRepository,
    private val hasher: PasswordHasher

) {
    suspend fun execute(user: User): User {
        val existing = userRepository.findByEmail(user.email)
        if (existing != null) throw IllegalStateException("duplicate email")

        val roleId = user.role?.id ?:
        throw IllegalArgumentException("Role ID is required")

        val roledb = catalogRepository.findRoleById(roleId)
            ?: throw IllegalStateException("the role doesn't exist")

        val allowedRoles = listOf("Musician", "Recruiter")
        if (!allowedRoles.contains(roledb.name))
            throw IllegalStateException("the role doesn't exist")


        val userSave = user.copy(
            password = hasher.hash(user.password),
            role = roledb
        )

        val saveUser = userRepository.save(userSave)
        val userId = saveUser.id ?: throw IllegalStateException("User ID was not generated")

        if (roledb.name.equals("Musician", ignoreCase = true)) {
            if (!profileRepository.create(userId))
                throw RuntimeException("Failed to create profile for musician")
        }

        return saveUser
    }
}
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
        if (existing != null) throw IllegalStateException("Email Duplicado")

        val roleId = user.role?.id ?:
        throw IllegalArgumentException("El rolId es requerido")

        val roledb = catalogRepository.findRoleById(roleId)
            ?: throw IllegalStateException("El rol no existe")

        val allowedRoles = listOf("Musician", "Recruiter")
        if (!allowedRoles.contains(roledb.name))
            throw IllegalStateException("El rol no existe")


        val userSave = user.copy(
            password = hasher.hash(user.password),
            role = roledb
        )

        val saveUser = userRepository.save(userSave)
        val userId = saveUser.id ?: throw IllegalStateException("UserId no fue generado")

        if (roledb.name.equals("Musician", ignoreCase = true)) {
            if (!profileRepository.create(userId))
                throw RuntimeException("Fallo al crear el perfil para el musico")
        }

        return saveUser
    }
}
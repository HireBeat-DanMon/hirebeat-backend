package com.ktor.api.hirebeat.modules.auth.application

import com.ktor.api.hirebeat.modules.auth.domain.service.TokenService
import com.ktor.api.hirebeat.common.infrastructure.security.PasswordHasher
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val hasher: PasswordHasher
) {
    suspend fun execute(email: String, plainPassword: String): Pair<String, String?> {
        val user = userRepository.findByEmail(email)
            ?: throw SecurityException("Contraseña o email invalio")

        if (!hasher.check(plainPassword, user.password)) {
            throw SecurityException("COntraseña o email invalido")
        }

        val token = tokenService.generateToken(user)
        val roleId = user.role?.id?.toString()

        return Pair(token, roleId)
    }
}
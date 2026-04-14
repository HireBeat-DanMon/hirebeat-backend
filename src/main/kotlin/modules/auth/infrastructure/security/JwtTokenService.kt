package com.ktor.api.hirebeat.modules.auth.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ktor.api.hirebeat.modules.auth.domain.service.TokenService
import com.ktor.api.hirebeat.modules.users.domain.model.User
import io.github.cdimascio.dotenv.dotenv
import java.util.Date

class JwtTokenService : TokenService {
    private val dotenv = dotenv()
    private val secret = dotenv["JWT_SECRET"] ?: "default"
    private val issuer = dotenv["JWT_ISSUER"] ?: "hirebeat-api"

    override fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(issuer)
            .withClaim("id", user.id.toString())
            .withClaim("email", user.email)
            .withClaim("role", user.role?.name ?: "GUEST")
            .withExpiresAt(Date(System.currentTimeMillis() + 2592000000))
            .sign(Algorithm.HMAC256(secret))
    }
}
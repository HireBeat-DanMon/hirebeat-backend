package com.ktor.api.hirebeat.common.infrastructure.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv

fun Application.configureSecurity() {
    val dotenv = dotenv()
    val secret = dotenv["JWT_SECRET"] ?: "default"
    val issuer = dotenv["JWT_ISSUER"] ?: "hirebeat-api"

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.subject != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
package com.ktor.api.hirebeat.common.infrastructure.security

interface PasswordHasher {
    fun hash(password: String): String
    fun check(plain: String, hashed: String): Boolean
}
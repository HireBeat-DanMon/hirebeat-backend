package com.ktor.api.hirebeat.common.infrastructure.security

import org.mindrot.jbcrypt.BCrypt

class BCryptHasher : PasswordHasher {
    override fun hash(password: String): String =
        BCrypt.hashpw(password, BCrypt.gensalt())

    override fun check(plain: String, hashed: String): Boolean =
        BCrypt.checkpw(plain, hashed)
}
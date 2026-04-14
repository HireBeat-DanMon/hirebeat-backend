package com.ktor.api.hirebeat.modules.auth.domain.service

import com.ktor.api.hirebeat.modules.users.domain.model.User

interface TokenService {
    fun generateToken(user: User): String
}
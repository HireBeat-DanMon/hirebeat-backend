package com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.users.domain.model.User
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val fullname : String,
    val email : String,
    val role: String
)

fun User.toResponse() = UserResponse(
    id = id ?: UUID.randomUUID(),
    fullname = fullname ?: "",
    email = email,
    role = role?.name ?: "GUEST"
)

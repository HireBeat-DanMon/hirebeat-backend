package com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Role
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SystemCatalogResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
){}

fun Role.toResponse() = SystemCatalogResponse(
    id = id ?: UUID.randomUUID(),
    name = name ?: throw IllegalArgumentException("Error con el nombre")
)
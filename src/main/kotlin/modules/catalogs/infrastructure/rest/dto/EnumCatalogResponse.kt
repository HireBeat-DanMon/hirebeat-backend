package com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.catalogs.domain.model.*
import kotlinx.serialization.Serializable

@Serializable
data class EnumCatalogResponse(
    val id: Int,
    val name: String
){}

fun Level.toResponse() = EnumCatalogResponse(
    id = rank,
    name = name
)
fun LinkType.toResponse() = EnumCatalogResponse(
    id = id,
    name = name
)

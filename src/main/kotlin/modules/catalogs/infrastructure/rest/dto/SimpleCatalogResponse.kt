package com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Genre
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Instrument
import kotlinx.serialization.Serializable

@Serializable
data class SimpleCatalogResponse(
    val id: Int ,
    val name: String
){}

fun Instrument.toResponse() = SimpleCatalogResponse(
    id = id ?: 0,
    name = name
)

fun Genre.toResponse() = SimpleCatalogResponse(
    id = id ?: 0,
    name = name
)


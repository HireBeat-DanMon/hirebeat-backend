package com.ktor.api.hirebeat.modules.profile.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto.UserResponse
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProfileResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email : String,
    val fullname : String,
    val city: String,
    val experience: Int,
    val descripcion: String,
    val imageUrl: String?,
    val genres: List<GenreResponse>,
    val instruments: List<InstrumentResponse>,
    val links: List<LinkResponse>
){}

@Serializable
data class LinkResponse(
    val name: String,
    val type: Int,
    val ref: String
){}

@Serializable
data class GenreResponse(
    val id: Int,
    val name: String
) {}

@Serializable
data class InstrumentResponse(
    val id: Int,
    val name: String,
    val level: String,
    val isPrincipal: Boolean
){}

fun Profile.toResponse() = ProfileResponse(
    id = id,
    email = user.email,
    fullname = user.fullname ?: "",
    city = city ?: "",
    experience = experience ?: 0,
    descripcion = descripcion ?: "",
    imageUrl = imageUrl,
    genres = genres.map {
        GenreResponse(
            it.id,
            it.name ?: ""
        )
    },
    instruments = instruments.map {
        InstrumentResponse(
            id = it.instrument.id,
            name = it.instrument.name ?: "",
            level = it.level.name,
            isPrincipal = it.isPrincipal
        )
    },
    links = links.map {
        LinkResponse(
            name = it.name,
            type = it.type.id,
            ref = it.ref
        )
    }
)
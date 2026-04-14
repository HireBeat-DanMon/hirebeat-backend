package com.ktor.api.hirebeat.modules.profile.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSummaryResponse(
    val fullname: String,
    val city: String,
    val experience: Int,
    val mainInstrument: String,
    val level: String,
    val genres: List<String>
)

fun Profile.toSummaryResponse() = ProfileSummaryResponse(
    fullname = user.fullname ?: "",
    city = city ?: "No especificada",
    experience = experience ?: 0,
    mainInstrument = instruments.firstOrNull()?.instrument?.name ?: "Ninguno",
    level = instruments.firstOrNull()?.level?.name ?: "N/A",
    genres = genres.map { it.name ?: "" }
)
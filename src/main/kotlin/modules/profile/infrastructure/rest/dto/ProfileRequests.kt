package com.ktor.api.hirebeat.modules.profile.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Genre
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Instrument
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Level
import com.ktor.api.hirebeat.modules.catalogs.domain.model.LinkType
import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import com.ktor.api.hirebeat.modules.profile.domain.model.ProfileInstrument
import com.ktor.api.hirebeat.modules.profile.domain.model.ProfileLink
import com.ktor.api.hirebeat.modules.users.domain.model.User
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProfileRequest(
    val city: String = "",
    val experience: Int = 0,
    val descripcion: String = "",
    val genres: List<Int> = emptyList(),
    val instruments: List<InstrumentSelectionRequest> = emptyList(),
    val links: List<PlataformasProfileRequest> = emptyList()
) {
    fun getErrors(): Map<String, Any> {
        val errors = mutableMapOf<String, Any>()

        if (city.isBlank()) errors["city"] = "La ciudad no puede estar vacía"
        if (experience < 0) errors["experience"] = "La experiencia no puede ser negativa"
        if (genres.isEmpty()) errors["genres"] = "Debes seleccionar al menos un género musical"

        if (descripcion.isNullOrBlank()) {
            errors["descripcion"] = "La descripción es obligatoria"
        } else if (descripcion.length > 500) {
            errors["descripcion"] = "La descripción no puede exceder los 500 caracteres"
        }

        // Validación de Instrumentos
        if (instruments.isEmpty()) {
            errors["instruments"] = "Debes agregar al menos un instrumento"
        } else {
            val errorInstrument = mutableMapOf<String, String>()
            val principalCount = instruments.count { it.isPrincipal }

            if (instruments.any { it.level < 0 || it.level > 5 }) errorInstrument["level"] = "Solo existen niveles del 0 al 5"
            if (instruments.any { it.instrumentId <= 0 }) errorInstrument["instrumentId"] = "ID de instrumento inválido"
            if (principalCount != 1) errorInstrument["isPrincipal"] = "Debe haber exactamente un instrumento principal"

            if (errorInstrument.isNotEmpty()) errors["instruments"] = errorInstrument
        }

        // Validación de Links
        if (links.isEmpty()) {
            errors["links"] = "Debes agregar al menos una red social"
        } else {
            val errorLinks = mutableMapOf<String, String>()
            if (links.any { it.name.isBlank() }) errorLinks["name"] = "Es obligatorio el nombre"
            if (links.any { it.ref.isBlank() }) errorLinks["ref"] = "Es requerida la referencia"
            if (links.any { it.type < 1 || it.type > 3 }) errorLinks["type"] = "Tipo inválido: 1.Redes 2.Phone 3. Mensajes"

            if (errorLinks.isNotEmpty()) errors["links"] = errorLinks
        }
        return errors
    }

    fun toDomain(userId: UUID) = Profile(
        id = UUID.randomUUID(),
        user = User(id = userId, email = "vacio@gmail.com", password = ""),
        city = city,
        experience = experience,
        descripcion = descripcion,
        genres = genres.map { Genre(id = it, name = "vacio") },
        instruments = instruments.map {
            ProfileInstrument(
                instrument = Instrument(id = it.instrumentId, name = "vacio"),
                level = Level.fromInt(it.level),
                isPrincipal = it.isPrincipal
            )
        },
        links = links.map {
            ProfileLink(
                name = it.name,
                type = LinkType.fromInt(it.type),
                ref = it.ref,
            )
        }
    )
}

@Serializable
data class InstrumentSelectionRequest(
    val instrumentId: Int = 0,
    val level: Int = 0,
    val isPrincipal: Boolean = false
){}

@Serializable
data class PlataformasProfileRequest(
    val name: String = "",
    val type: Int = 0,
    val ref: String = ""
) {}
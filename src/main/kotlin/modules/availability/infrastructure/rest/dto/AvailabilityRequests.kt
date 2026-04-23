package com.ktor.api.hirebeat.modules.availability.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.UUID

@Serializable
data class CreateBlockedSlotRequest(
    val startTime: String,
    val endTime: String,
    val reason: String? = null
) {
    fun getErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        try {
            val start = LocalDateTime.parse(startTime)
            val end = LocalDateTime.parse(endTime)
            if (!start.isBefore(end)) errors["dates"] = "La fecha de inicio debe ser menor a la final"
        } catch (e: DateTimeParseException) {
            errors["format"] = "El formato de fecha debe ser YYYY-MM-DDTHH:MM:SS"
        }
        return errors
    }

    fun toDomain(placeholderId: UUID = UUID.randomUUID()) = BlockedSlot(
        musicianProfileId = placeholderId,
        startTime = LocalDateTime.parse(startTime),
        endTime = LocalDateTime.parse(endTime),
        reason = reason
    )
}
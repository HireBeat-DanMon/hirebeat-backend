package com.ktor.api.hirebeat.modules.gig_requests.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CreateGigRequestDto(
    val musicianProfileId: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val paymentOffered: Double,
    val messageDetails: String?
) {
    fun getErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (location.isBlank()) errors["location"] = "El lugar/ubicación es obligatorio"
        if (paymentOffered <= 0.0) errors["paymentOffered"] = "El pago ofrecido debe ser mayor a 0"

        try {
            val start = LocalDateTime.parse(startTime)
            val end = LocalDateTime.parse(endTime)
            if (!start.isBefore(end)) {
                errors["time"] = "La hora de inicio debe ser anterior a la hora de fin"
            }
        } catch (e: Exception) {
            errors["format"] = "El formato de fecha/hora es inválido"
        }

        return errors
    }

    fun toDomain() = GigRequest(
        recruiter = null,
        musicianProfileId = UUID.fromString(musicianProfileId),
        startTime = LocalDateTime.parse(startTime),
        endTime = LocalDateTime.parse(endTime),
        location = location,
        paymentOffered = paymentOffered,
        messageDetails = messageDetails
    )
}

@Serializable
data class CreateGigRequestResponseDto(
    val id: String,
    val message: String
)

@Serializable
data class UpdateStatusDto(val status: String)

@Serializable
data class GigRequestResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val recruiterId: UUID,
    @Serializable(with = UUIDSerializer::class) val musicianProfileId: UUID,
    val startTime: String,
    val endTime: String,
    val location: String,
    val paymentOffered: Double,
    val messageDetails: String?,
    val status: String,
    val createdAt: String?
)

fun GigRequest.toResponse() = GigRequestResponse(
    id = id ?: UUID.randomUUID(),
    recruiterId = recruiter?.id ?: UUID.randomUUID(),
    musicianProfileId = musicianProfileId,
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    location = location,
    paymentOffered = paymentOffered,
    messageDetails = messageDetails,
    status = status.name,
    createdAt = createdAt?.toString()
)
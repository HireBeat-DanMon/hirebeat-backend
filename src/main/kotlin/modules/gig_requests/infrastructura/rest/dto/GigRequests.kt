package com.ktor.api.hirebeat.modules.gig_requests.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.users.domain.model.User
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CreateGigRequestDto(
    val musicianProfileId: String,
    val startTime: String,
    val endTime: String,
    val messageDetails: String?
) {
    fun toDomain(recruiterId: UUID) = GigRequest(
        recruiter = User(id = recruiterId, email = "", password = ""),
        musicianProfileId = UUID.fromString(musicianProfileId),
        startTime = LocalDateTime.parse(startTime),
        endTime = LocalDateTime.parse(endTime),
        messageDetails = messageDetails
    )
}

@Serializable
data class UpdateStatusDto(val status: String)

@Serializable
data class GigRequestResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val recruiterId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val musicianProfileId: UUID,
    val startTime: String,
    val endTime: String,
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
    messageDetails = messageDetails,
    status = status.name,
    createdAt = createdAt?.toString()
)
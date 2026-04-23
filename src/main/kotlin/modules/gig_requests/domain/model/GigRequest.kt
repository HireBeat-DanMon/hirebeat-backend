package com.ktor.api.hirebeat.modules.gig_requests.domain.model

import com.ktor.api.hirebeat.modules.users.domain.model.User
import java.time.LocalDateTime
import java.util.UUID

enum class RequestStatus {
    PENDIENTE, ACEPTADA, RECHAZADA, CANCELADA, COMPLETADA
}

data class GigRequest(
    val id: UUID? = null,
    val recruiter: User? = null,
    val musicianProfileId: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val paymentOffered: Double,
    val messageDetails: String?,
    val status: RequestStatus = RequestStatus.PENDIENTE,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    init {
        require(startTime.isBefore(endTime)) { "La hora de inicio debe ser anterior a la hora de fin" }
    }
}
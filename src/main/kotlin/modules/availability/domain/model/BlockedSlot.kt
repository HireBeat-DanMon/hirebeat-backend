package com.ktor.api.hirebeat.modules.availability.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class BlockedSlot(
    val id: UUID? = null,
    val musicianProfileId: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val reason: String? = null,
    val isAutomatic: Boolean = false
) {
    init {
        require(startTime.isBefore(endTime)) { "La hora de inicio debe ser anterior a la hora de fin" }
    }
}
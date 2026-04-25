package com.ktor.api.hirebeat.modules.availability.infrastructure.rest.dto

import com.ktor.api.hirebeat.common.infrastructure.serialization.UUIDSerializer
import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BlockedSlotResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val startTime: String,
    val endTime: String,
    val reason: String?,
    val isAutomatic: Boolean
){}

fun BlockedSlot.toResponse() = BlockedSlotResponse(
    id = id ?: UUID.randomUUID(),
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    reason = reason,
    isAutomatic = isAutomatic
)
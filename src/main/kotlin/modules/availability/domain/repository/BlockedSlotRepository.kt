package com.ktor.api.hirebeat.modules.availability.domain.repository

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import java.time.LocalDateTime
import java.util.UUID

interface BlockedSlotRepository {
    suspend fun save(slot: BlockedSlot): BlockedSlot
    suspend fun findByProfileIdAndDateRange(profileId: UUID, start: LocalDateTime, end: LocalDateTime): List<BlockedSlot>
    suspend fun delete(id: UUID, profileId: UUID): Boolean
    suspend fun hasOverlap(profileId: UUID, start: LocalDateTime, end: LocalDateTime): Boolean
}
package com.ktor.api.hirebeat.modules.availability.infrastructure.persistence

import com.ktor.api.hirebeat.modules.availability.domain.model.BlockedSlot
import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class PostgresBlockedSlotRepository : BlockedSlotRepository {

    override suspend fun save(slot: BlockedSlot): BlockedSlot = transaction {
        val newId = slot.id ?: UUID.randomUUID()
        BlockedSlotTable.insert {
            it[id] = newId
            it[profileId] = slot.musicianProfileId
            it[startTime] = slot.startTime
            it[endTime] = slot.endTime
            it[reason] = slot.reason
            it[isAutomatic] = slot.isAutomatic
        }
        slot.copy(id = newId)
    }

    override suspend fun findByProfileIdAndDateRange(
        profileId: UUID, start: LocalDateTime, end: LocalDateTime
    ): List<BlockedSlot> = transaction {
        BlockedSlotTable.select {
            (BlockedSlotTable.profileId eq profileId) and
                    (BlockedSlotTable.startTime less end) and
                    (BlockedSlotTable.endTime greater start)
        }.orderBy(BlockedSlotTable.startTime to SortOrder.ASC).map {
            BlockedSlot(
                id = it[BlockedSlotTable.id],
                musicianProfileId = it[BlockedSlotTable.profileId],
                startTime = it[BlockedSlotTable.startTime],
                endTime = it[BlockedSlotTable.endTime],
                reason = it[BlockedSlotTable.reason],
                isAutomatic = it[BlockedSlotTable.isAutomatic]
            )
        }
    }

    override suspend fun delete(id: UUID, profileId: UUID): Boolean = transaction {
        val deletedRows = BlockedSlotTable.deleteWhere {
            (BlockedSlotTable.id eq id) and (BlockedSlotTable.profileId eq profileId)
        }
        deletedRows > 0
    }

    override suspend fun hasOverlap(profileId: UUID, start: LocalDateTime, end: LocalDateTime): Boolean = transaction {
        val count = BlockedSlotTable.select {
            (BlockedSlotTable.profileId eq profileId) and
                    (BlockedSlotTable.startTime less end) and
                    (BlockedSlotTable.endTime greater start)
        }.count()
        count > 0
    }
}
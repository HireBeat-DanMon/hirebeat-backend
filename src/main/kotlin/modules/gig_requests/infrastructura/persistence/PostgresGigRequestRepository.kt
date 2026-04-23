package com.ktor.api.hirebeat.modules.gig_requests.infrastructure.persistence

import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.model.RequestStatus
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import com.ktor.api.hirebeat.modules.users.domain.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class PostgresGigRequestRepository : GigRequestRepository {

    override suspend fun save(request: GigRequest): GigRequest = transaction {
        val newId = UUID.randomUUID()
        GigRequestTable.insert {
            it[id] = newId
            it[recruiterId] = request.recruiter!!.id!!
            it[musicianProfileId] = request.musicianProfileId
            it[startTime] = request.startTime
            it[endTime] = request.endTime
            it[messageDetails] = request.messageDetails
            it[status] = request.status
        }
        request.copy(id = newId)
    }

    override suspend fun findById(id: UUID): GigRequest? = transaction {
        GigRequestTable.select { GigRequestTable.id eq id }.map { rowToGigRequest(it) }.singleOrNull()
    }

    override suspend fun findByMusicianProfileId(profileId: UUID): List<GigRequest> = transaction {
        GigRequestTable.select { GigRequestTable.musicianProfileId eq profileId }
            .orderBy(GigRequestTable.createdAt to SortOrder.DESC).map { rowToGigRequest(it) }
    }

    override suspend fun findByRecruiterId(recruiterId: UUID): List<GigRequest> = transaction {
        GigRequestTable.select { GigRequestTable.recruiterId eq recruiterId }
            .orderBy(GigRequestTable.createdAt to SortOrder.DESC).map { rowToGigRequest(it) }
    }

    override suspend fun updateStatus(id: UUID, status: RequestStatus): Boolean = transaction {
        GigRequestTable.update({ GigRequestTable.id eq id }) {
            it[GigRequestTable.status] = status
            it[updatedAt] = LocalDateTime.now()
        } > 0
    }

    private fun rowToGigRequest(row: ResultRow) = GigRequest(
        id = row[GigRequestTable.id],
        recruiter = User(id = row[GigRequestTable.recruiterId], email = "", password = ""),
        musicianProfileId = row[GigRequestTable.musicianProfileId],
        startTime = row[GigRequestTable.startTime],
        endTime = row[GigRequestTable.endTime],
        messageDetails = row[GigRequestTable.messageDetails],
        status = row[GigRequestTable.status],
        createdAt = row[GigRequestTable.createdAt],
        updatedAt = row[GigRequestTable.updatedAt]
    )
}
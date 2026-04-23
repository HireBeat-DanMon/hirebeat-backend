package com.ktor.api.hirebeat.modules.reviews.infrastructure.persistence

import com.ktor.api.hirebeat.modules.reviews.domain.model.Review
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PostgresReviewRepository : ReviewRepository {

    override suspend fun save(review: Review): Review = transaction {
        val newId = UUID.randomUUID()
        ReviewTable.insert {
            it[id] = newId
            it[reviewerId] = review.reviewer!!.id!!
            it[musicianProfileId] = review.musicianProfileId
            it[rating] = review.rating
            it[comment] = review.comment
        }
        review.copy(id = newId)
    }

    override suspend fun findByMusicianProfileId(profileId: UUID): List<Review> = transaction {
        (ReviewTable innerJoin UserTable)
            .select { ReviewTable.musicianProfileId eq profileId }
            .orderBy(ReviewTable.createdAt to SortOrder.DESC)
            .map { row ->
                Review(
                    id = row[ReviewTable.id],
                    reviewer = User(
                        id = row[UserTable.id],
                        email = row[UserTable.email],
                        fullname = row[UserTable.fullname],
                        password = ""
                    ),
                    musicianProfileId = row[ReviewTable.musicianProfileId],
                    rating = row[ReviewTable.rating],
                    comment = row[ReviewTable.comment],
                    createdAt = row[ReviewTable.createdAt]
                )
            }
    }

    override suspend fun delete(id: UUID, reviewerId: UUID): Boolean = transaction {
        val deletedRows = ReviewTable.deleteWhere {
            (ReviewTable.id eq id) and (ReviewTable.reviewerId eq reviewerId)
        }
        deletedRows > 0
    }


    override suspend fun findAll(): List<Review> = transaction {
        (ReviewTable innerJoin UserTable)
            .selectAll()
            .orderBy(ReviewTable.createdAt to SortOrder.DESC)
            .map { row ->
                Review(
                    id = row[ReviewTable.id],
                    reviewer = User(
                        id = row[UserTable.id],
                        email = row[UserTable.email],
                        fullname = row[UserTable.fullname],
                        password = ""
                    ),
                    musicianProfileId = row[ReviewTable.musicianProfileId],
                    rating = row[ReviewTable.rating],
                    comment = row[ReviewTable.comment],
                    createdAt = row[ReviewTable.createdAt]
                )
            }
    }

}
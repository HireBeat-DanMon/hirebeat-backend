package com.ktor.api.hirebeat.modules.reviews.infrastructure.persistence

import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ReviewTable : Table("reviews") {
    val id = uuid("id")
    val reviewerId = uuid("reviewer_id") references UserTable.id
    val musicianProfileId = uuid("musician_profile_id") references ProfileTable.id
    val rating = integer("rating")
    val comment = text("comment")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}
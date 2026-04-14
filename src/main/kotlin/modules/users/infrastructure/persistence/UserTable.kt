package com.ktor.api.hirebeat.modules.users.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.RoleTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable : Table("users"){
    val id = uuid("id")
    val rolId = uuid("rol_id") references RoleTable.id
    val fullname = varchar("fullname", 255).nullable()
    val email = varchar("email", 150).uniqueIndex()
    val password = text("password")

    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
    val deleted = bool("deleted").default(false)

    override val primaryKey = PrimaryKey(id)
}

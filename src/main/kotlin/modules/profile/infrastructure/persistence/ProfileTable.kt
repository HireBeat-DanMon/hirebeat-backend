package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence

import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.Table

object ProfileTable : Table("musician_profile"){
    val id = uuid("id")
    val userId = uuid("user_id") references UserTable.id
    val city = varchar("city", 100).nullable()
    val experience = integer("experience").nullable()
    val descripcion = text("descripcion").nullable()

    override val primaryKey = PrimaryKey(id)
}

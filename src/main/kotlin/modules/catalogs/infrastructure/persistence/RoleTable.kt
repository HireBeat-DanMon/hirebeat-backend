package com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object RoleTable : Table("roles") {
    val id = uuid("id")
    val name = varchar("name", 150).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
package com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object GenreTable : Table("genre") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 150).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
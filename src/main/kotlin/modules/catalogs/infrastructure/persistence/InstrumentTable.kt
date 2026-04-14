package com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object InstrumentTable : Table("instrumentt") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 150).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
package com.ktor.api.hirebeat.common.infrastructure

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Instrument
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.GenreTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.InstrumentTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.RoleTable
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileGenresTable
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileInstrumentsTable
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileLinksTable
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable
import com.ktor.api.hirebeat.modules.reviews.infrastructure.persistence.ReviewTable
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object DatabaseFactory {
    private val dotenv = dotenv()

    fun init() {
        val host = dotenv["DB_HOST"]
        val port = dotenv["DB_PORT"]
        val name = dotenv["DB_NAME"]

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://$host:$port/$name"
            username = dotenv["DB_USER"]
            password = dotenv["DB_PASSWORD"]
            maximumPoolSize = 10

            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                UserTable,
                RoleTable, InstrumentTable, GenreTable,
                ProfileTable, ProfileInstrumentsTable, ProfileGenresTable, ProfileLinksTable,
                ReviewTable
            )
        }

        DatabaseSeeder.seed()
    }

}


package com.ktor.api.hirebeat.common.infrastructure


import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database


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


    }

}


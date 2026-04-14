package com.ktor.api.hirebeat.modules.users.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Role
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.RoleTable
import com.ktor.api.hirebeat.modules.users.domain.model.*
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.mapper.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class PostgresUserRepository : UserRepository {

    override suspend fun findByEmail(email: String): User? = transaction {
        (UserTable innerJoin RoleTable)
            .select { (UserTable.email eq email) and (UserTable.deleted eq false) }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun findById(id: UUID): User? = transaction {
        (UserTable innerJoin RoleTable)
            .select { (UserTable.id eq id) and (UserTable.deleted eq false) }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun findByAll(): List<User> = transaction {
        (UserTable innerJoin RoleTable)
            .select { UserTable.deleted eq false }
            .map { it.toUser() }
    }

    override suspend fun save(user: User): User = transaction {
        val newId = user.id ?: UUID.randomUUID()

        val roleFromDb = user.role
            ?: throw IllegalArgumentException("Role problem")

        UserTable.insert {
            it[id] = newId
            it[fullname] = user.fullname
            it[rolId] = roleFromDb.id!!
            it[email] = user.email
            it[password] = user.password
            it[deleted] = false
        }
        user.copy(id = newId)
    }

    override suspend fun update(user: User): User? {
        val id = user.id ?: return null
        return transaction {
            val updateRows = UserTable.update({ UserTable.id eq id }) {
                it[fullname] = user.fullname
                it[email] = user.email
                it[password] = user.password
                it[updatedAt] = LocalDateTime.now()
            }
            if (updateRows > 0) user else null
        }
    }

    override suspend fun delete(id: UUID): Boolean = transaction {
        val updatedRows = UserTable.update({ UserTable.id eq id }) {
            it[deleted] = true
            it[updatedAt] = LocalDateTime.now()
        }
        updatedRows > 0
    }
}
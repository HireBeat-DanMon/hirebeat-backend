package com.ktor.api.hirebeat.modules.users.infrastructure.persistence.mapper

import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.mapper.toRole
import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser(): User = User(
    id = this[UserTable.id],
    fullname = this[UserTable.fullname],
    email = this[UserTable.email],
    password = this[UserTable.password],
    role = this.toRole(),
    createdAt = this[UserTable.createdAt],
    updateAt = this[UserTable.updatedAt],
    deleted = this[UserTable.deleted]
)
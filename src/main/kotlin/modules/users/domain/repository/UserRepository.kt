package com.ktor.api.hirebeat.modules.users.domain.repository

import com.ktor.api.hirebeat.modules.users.domain.model.User
import java.util.UUID

interface UserRepository{
    suspend fun findByEmail (email : String) : User?

    suspend fun findById(id : UUID) : User?
    suspend fun findByAll() : List<User>?
    suspend fun save ( user : User) : User
    suspend fun update ( user : User) : User?
    suspend fun delete (id : UUID) : Boolean

}

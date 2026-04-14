package com.ktor.api.hirebeat.modules.users.domain.model

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Role
import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID? = null,
    val email : String,
    val password : String,
    val fullname: String? = null,
    val role : Role? = null,
    val createdAt : LocalDateTime? = null,
    val updateAt : LocalDateTime? = null,
    val deleted : Boolean = false
){
    init {
        require(email.contains("@")) { "El formato del email no es válido" }
    }
}

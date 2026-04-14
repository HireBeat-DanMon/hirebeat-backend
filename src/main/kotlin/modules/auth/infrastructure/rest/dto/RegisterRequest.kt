package com.ktor.api.hirebeat.modules.auth.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Role
import com.ktor.api.hirebeat.modules.users.domain.model.User
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RegisterRequest(
    val fullname: String? = null,
    val email: String = "",
    val password: String = "",
    val roleId: String ? = null,
    val roleName : String ? = ""
){
    fun getErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (fullname != null && fullname.length <= 10) {
            errors["fullname"] = "Si se proporciona, debe ser mayor a 10 caracteres"
        }
        if (email.isBlank() || !email.contains("@")) {
            errors["email"] = "Email es requerido y debe ser válido"
        }
        val errorsPsw = mutableListOf<String>()

        if (password.isBlank()) {
            errors["password"] = "La contraseña es requerida"
        } else {
            if (password.length < 8)
                errorsPsw.add("al menos 8 caracteres")
            if (!password.contains(Regex("[A-Z]")))
                errorsPsw.add("una mayúscula")
            if (!password.contains(Regex("[a-z]")))
                errorsPsw.add("una minúscula")
            if (!password.contains(Regex("\\d")))
                errorsPsw.add("un número")
            if (errorsPsw.isNotEmpty())
                errors["password"] = "La contraseña debe tener: ${errorsPsw.joinToString(", ")}."
        }

        if (roleId.isNullOrBlank())
            errors["roleId"] = "El rol es obligatorio"
        return errors
    }

    fun toDomain() = User(
        email = email,
        password = password,
        fullname = fullname,
        role = Role(
            id = UUID.fromString(roleId),
            name = roleName
        )
    )
}
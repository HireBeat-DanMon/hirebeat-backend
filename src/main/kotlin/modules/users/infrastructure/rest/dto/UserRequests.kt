package com.ktor.api.hirebeat.modules.users.infrastructure.rest.dto

import com.ktor.api.hirebeat.modules.users.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val fullname: String = "",
    val email: String= "",
    val password: String= "",
    val passwordOld: String= ""
){
    fun getErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (fullname.length <= 10) {
            errors["fullname"] = "Si se proporciona, debe ser mayor a 10 caracteres"
        }
        if (email.isNullOrBlank() || !email.contains("@")) {
            errors["email"] = "Email es requerido y debe ser válido"
        }

        if (passwordOld.isBlank()) {
            errors["passwordOld"] = "La contraseña antigua es requerida"
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

        return errors
    }

    fun toDomain() = User(
        fullname = fullname,
        email = email,
        password = password,
        role = null
    )
}

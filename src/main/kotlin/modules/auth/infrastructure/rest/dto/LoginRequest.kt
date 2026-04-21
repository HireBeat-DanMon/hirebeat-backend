package com.ktor.api.hirebeat.modules.auth.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String = "",
    val password: String = ""
) {
    fun getErrors(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (email.isBlank() || !email.contains("@")) {
            errors["email"] = "El email es requerido, o falla el formato"
        }

        val passwordRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""")
        if (password.isBlank()) {
            errors["password"] = "La contraseña es requerida"
        } else if (!password.matches(passwordRegex)) {
            errors["password"] = "La contraseña tiene un formato invalido"
        }
        return errors
    }
}
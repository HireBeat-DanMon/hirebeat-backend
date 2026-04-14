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
            errors["email"] = "Valid email is required"
        }

        val passwordRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""")
        if (password.isBlank()) {
            errors["password"] = "Password is required"
        } else if (!password.matches(passwordRegex)) {
            errors["password"] = "Invalid password format"
        }
        return errors
    }
}
package com.ktor.api.hirebeat.modules.catalogs.domain.model

enum class Level(val rank: Int) {
    BASICO(1),
    PRINCIPIANTE(2),
    INTERMEDIO(3),
    AVANZADO(4),
    PROFESIONAL(5);

    companion object {
        fun fromInt(value: Int) = values().find { it.rank == value } ?: BASICO
    }
}
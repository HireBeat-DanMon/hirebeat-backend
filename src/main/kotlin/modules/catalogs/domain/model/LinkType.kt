package com.ktor.api.hirebeat.modules.catalogs.domain.model

enum class LinkType(val id: Int) {
    OTHER(99),
    SOCIAL_MEDIA(1),
    PHONE_NUMBER(2),
    MESSAGE(3);

    companion object {
        fun fromInt(value: Int) = values().find { it.id == value } ?: SOCIAL_MEDIA
    }
}
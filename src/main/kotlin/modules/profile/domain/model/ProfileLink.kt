package com.ktor.api.hirebeat.modules.profile.domain.model

import com.ktor.api.hirebeat.modules.catalogs.domain.model.LinkType


data class ProfileLink(
    val name: String,
    val type: LinkType,
    val ref: String
)
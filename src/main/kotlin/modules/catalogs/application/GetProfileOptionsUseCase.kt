package com.ktor.api.hirebeat.modules.catalogs.application

import com.ktor.api.hirebeat.modules.catalogs.domain.model.*


class GetProfileOptionsUseCase {
    fun execute() = Pair(
        Level.entries,
        LinkType.entries
    )
}
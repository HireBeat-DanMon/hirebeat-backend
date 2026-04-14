package com.ktor.api.hirebeat.modules.catalogs.application

import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository

class GetGenreUseCase(
    private val repository: CatalogRepository
) {
    suspend fun execute () = repository.findAllGenres()
}
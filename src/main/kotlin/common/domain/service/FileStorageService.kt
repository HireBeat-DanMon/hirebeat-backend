package com.ktor.api.hirebeat.common.domain.service

interface FileStorageService {
    suspend fun uploadImage(fileBytes: ByteArray, fileName: String): String
}
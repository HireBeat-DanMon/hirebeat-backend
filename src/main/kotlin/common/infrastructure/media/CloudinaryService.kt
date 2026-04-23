package com.ktor.api.hirebeat.common.infrastructure.media

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.ktor.api.hirebeat.common.domain.service.FileStorageService
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CloudinaryService : FileStorageService {
    private val dotenv = dotenv()
    private val cloudinary = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", dotenv["CLOUDINARY_CLOUD_NAME"],
            "api_key", dotenv["CLOUDINARY_API_KEY"],
            "api_secret", dotenv["CLOUDINARY_API_SECRET"]
        )
    )

    override suspend fun uploadImage(fileBytes: ByteArray, fileName: String): String = withContext(Dispatchers.IO) {
        val params = ObjectUtils.asMap(
            "public_id", "hirebeat/profiles/${fileName.substringBeforeLast(".")}",
            "overwrite", true,
            "resource_type", "image"
        )
        val uploadResult = cloudinary.uploader().upload(fileBytes, params)
        return@withContext uploadResult["secure_url"].toString()
    }
}
package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.rumigor.cookbook.data.api.UploadImageApi
import ru.rumigor.cookbook.data.model.ImageServerResponse
import java.io.File
import javax.inject.Inject

class UploadImageImpl @Inject constructor(
    private val uploadImageApi: UploadImageApi
): UploadImage{
    override fun uploadImage(filePath: String): Observable<ImageServerResponse> {
        val file = File(filePath)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        return uploadImageApi
            .uploadImage("333b91ad4eeb96cdf3b699c0cf1b2c4e",body)
            .toObservable()
    }
}
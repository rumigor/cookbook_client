package ru.rumigor.cookbook.data.repository


import io.reactivex.rxjava3.core.Observable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response

import ru.rumigor.cookbook.data.api.UploadImageApi

import java.io.File
import javax.inject.Inject

class UploadImageImpl @Inject constructor(
    private val uploadImageApi: UploadImageApi
) : UploadImage {
    override fun uploadImage(filePath: String): Observable<Response<ResponseBody>> {
        val file = File(filePath)
        println(filePath)
        val body = MultipartBody.Part.createFormData(
            "file", file.name,
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        return uploadImageApi
            .uploadImage(1, body)
            .toObservable()
    }
}
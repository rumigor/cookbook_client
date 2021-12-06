package ru.rumigor.cookbook.data.api

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import ru.rumigor.cookbook.data.model.ImageServerResponse

interface UploadImageApi {

    @Multipart
    @POST("/1/upload")
    fun uploadImage(@Query("key") key: String, @Part image: MultipartBody.Part): Single<ImageServerResponse>

}
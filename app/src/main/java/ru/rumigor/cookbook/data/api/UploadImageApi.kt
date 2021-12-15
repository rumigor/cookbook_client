package ru.rumigor.cookbook.data.api


import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface UploadImageApi {

    @Multipart
    @POST("/api/v1/file/upload")
    fun uploadImage(@Part file: MultipartBody.Part): Single<Response<ResponseBody>>

}
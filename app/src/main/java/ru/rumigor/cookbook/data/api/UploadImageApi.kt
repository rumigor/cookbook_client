package ru.rumigor.cookbook.data.api


import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface UploadImageApi {

    @Multipart
    @POST("/file/upload")
    fun uploadImage(@Query ("userId")userId: Int, @Part file: MultipartBody.Part): Single<Response<ResponseBody>>

}
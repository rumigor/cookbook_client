package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import ru.rumigor.cookbook.data.model.FileResponse
import ru.rumigor.cookbook.data.model.ImageServerResponse

interface UploadImage {
    fun uploadImage(filePath: String): Observable<Response<ResponseBody>>
}
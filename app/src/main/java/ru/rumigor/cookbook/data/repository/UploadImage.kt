package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Observable
import ru.rumigor.cookbook.data.model.ImageServerResponse

interface UploadImage {
    fun uploadImage(filePath: String): Observable<ImageServerResponse>
}
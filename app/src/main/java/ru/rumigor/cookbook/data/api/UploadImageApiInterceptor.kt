package ru.rumigor.cookbook.data.api

import okhttp3.Interceptor
import okhttp3.Response

object UploadImageApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Content-Type", "image/jpeg")
                .build()
        )
}
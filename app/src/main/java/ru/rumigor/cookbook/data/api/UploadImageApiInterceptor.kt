package ru.rumigor.cookbook.data.api

import okhttp3.Interceptor
import okhttp3.Response

object UploadImageApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("key", "569AHPRW898b721fa9229e1836487e2a5b7d86ec")
                .build()
        )
}
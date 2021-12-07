package ru.rumigor.cookbook.data.api

import okhttp3.Interceptor
import okhttp3.Response

object CookbookApiInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("cookbook", "http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com")
                .build()
        )
}
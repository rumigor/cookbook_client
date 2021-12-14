package ru.rumigor.cookbook.data.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject




class AuthorizationInterceptor(username: String, password: String): Interceptor {


    private var credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", credentials)
                .build()
        )
}
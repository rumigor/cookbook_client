package ru.rumigor.cookbook.data.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import ru.rumigor.cookbook.AppPreferences
import javax.inject.Inject




class AuthorizationInterceptor @Inject constructor(): Interceptor {



    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", Credentials.basic(AppPreferences.username!!, AppPreferences.password!!))
                .build()
        )
}
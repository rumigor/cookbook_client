package ru.rumigor.cookbook.data.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import ru.rumigor.cookbook.AppPreferences
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor() : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var username = ""
        var password = ""
        AppPreferences.username?.let {
            username = it
        }
        AppPreferences.password?.let {
            password = it
        }
        return if (username != "" && password != "") {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader(
                        "Authorization",
                        Credentials.basic(username, password)
                    )
                    .build()
            )
        } else chain.proceed(
            chain.request()
                .newBuilder()
                .build()
        )
    }
}
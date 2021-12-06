package ru.rumigor.cookbook.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.rumigor.cookbook.data.api.UploadImageApi
import ru.rumigor.cookbook.data.api.UploadImageApiInterceptor
import javax.inject.Named

@Module
class UploadImageApiModule {
    @Named("upload_image_api")
    @Provides
    fun provideBaseUrlProd(): String = "https://api.imgbb.com/"

    @Reusable
    @Provides
    fun provideGitHubApi(@Named("upload_image_api") baseUrl: String): UploadImageApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UploadImageApi::class.java)
}
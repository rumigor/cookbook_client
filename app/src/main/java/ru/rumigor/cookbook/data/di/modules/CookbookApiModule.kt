package ru.rumigor.cookbook.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.rumigor.cookbook.data.api.CookbookApi
import ru.rumigor.cookbook.data.api.CookbookApiInterceptor
import javax.inject.Named

@Module
class CookbookApiModule {

    @Named("cookbook_api")
    @Provides
    fun provideBaseUrlProd(): String = "http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/"

    @Reusable
    @Provides
    fun provideGitHubApi(@Named("cookbook_api") baseUrl: String): CookbookApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(CookbookApiInterceptor)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CookbookApi::class.java)
}
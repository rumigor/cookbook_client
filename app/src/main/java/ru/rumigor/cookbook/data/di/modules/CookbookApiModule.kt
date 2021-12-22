package ru.rumigor.cookbook.data.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.api.AuthorizationInterceptor
import ru.rumigor.cookbook.data.api.CookbookApi
import javax.inject.Named
import javax.inject.Singleton
import android.content.SharedPreferences


@Module
class CookbookApiModule {

    @Named("cookbook_api")
    @Provides
    fun provideBaseUrlProd(): String =
        "http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/"


    @Provides
    fun provideAppPreferences(ctx: Context): AppPreferences{
        return AppPreferences
    }

    @Reusable
    @Provides
    fun provideCookBookApi(@Named("cookbook_api") baseUrl: String, authBasicInterceptor: AuthorizationInterceptor?): CookbookApi {
        if (authBasicInterceptor != null) {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(authBasicInterceptor)
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CookbookApi::class.java)
        } else {
            return Retrofit.Builder()
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
                .create(CookbookApi::class.java)
        }
    }
}
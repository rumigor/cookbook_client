package ru.rumigor.cookbook.data.di.modules

import dagger.Binds
import dagger.Module
import okhttp3.Interceptor
import ru.rumigor.cookbook.data.api.AuthorizationInterceptor

@Module
abstract class AuthorizationInterceptorModule {
    @Binds
    abstract  fun bindAuthorizationInterceptor(authorizationInterceptor: AuthorizationInterceptor): Interceptor
}
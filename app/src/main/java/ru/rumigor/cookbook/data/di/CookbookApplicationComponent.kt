package ru.rumigor.cookbook.data.di

import android.content.Context
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.rumigor.cookbook.CookbookApp
import ru.rumigor.cookbook.data.di.modules.CookBookModule
import ru.rumigor.cookbook.data.di.modules.CookbookApiModule
import ru.rumigor.cookbook.scheduler.Schedulers
import javax.inject.Singleton

@Singleton
@Component(
    modules =[AndroidInjectionModule::class, CookBookModule::class, CookbookApiModule::class]
)
interface CookbookApplicationComponent : AndroidInjector<CookbookApp> {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun withContext(context: Context): Builder

        @BindsInstance
        fun withSchedulers(schedulers: Schedulers): Builder

        fun build(): CookbookApplicationComponent
    }
}
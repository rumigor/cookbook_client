package ru.rumigor.cookbook

import com.github.terrakok.cicerone.Cicerone
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.rumigor.cookbook.data.di.CookbookApplicationComponent
import ru.rumigor.cookbook.data.di.DaggerCookbookApplicationComponent

import ru.rumigor.cookbook.scheduler.DefaultSchedulers

class CookbookApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<CookbookApp> = cookbookApplicationComponent

    private val cookbookApplicationComponent: CookbookApplicationComponent by lazy {
        DaggerCookbookApplicationComponent
            .builder()
            .withContext(applicationContext)
            .apply{
                withSchedulers(DefaultSchedulers())
            }
            .build()
    }



}
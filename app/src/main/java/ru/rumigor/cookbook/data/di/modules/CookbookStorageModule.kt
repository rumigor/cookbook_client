package ru.rumigor.cookbook.data.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.rumigor.cookbook.data.storage.CookbookDatabase

@Module
class CookbookStorageModule {
    @Persisted
    @Provides
    fun provideDrinksDatabaseStorage(context: Context): CookbookDatabase =
        Room.databaseBuilder(context, CookbookDatabase::class.java, "drinks.db")
            .fallbackToDestructiveMigration()
            .build()

    @InMemory
    @Provides
    fun provideDrinksInMemoryDatabaseStorage(context: Context): CookbookDatabase =
        Room.inMemoryDatabaseBuilder(context, CookbookDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
}
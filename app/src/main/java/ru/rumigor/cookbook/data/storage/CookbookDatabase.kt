package ru.rumigor.cookbook.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.model.Recipe

@Database(exportSchema = false, entities = [FavoriteRecipe::class], version = 1)
abstract class CookbookDatabase: RoomDatabase() {
    abstract fun cookbookDao(): CookbookDao
}
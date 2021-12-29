package ru.rumigor.cookbook.data.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.model.Recipe
import java.util.*

@Dao
interface CookbookDao {
    @Query("SELECT * FROM recipes")
    fun loadFavoritesRecipes(): Observable<List<FavoriteRecipe>>

    @Query("SELECT * FROM recipes WHERE id LIKE :recipeId")
    fun loadFavoriteRecipe(recipeId: String): Single<FavoriteRecipe>

    @Query("SELECT * FROM recipes WHERE title LIKE '%'|| :name || '%'")
    fun favoriteSearch(name: String): Observable<List<FavoriteRecipe>>

    @Query("DELETE FROM recipes WHERE id LIKE :recipeId")
    fun deleteFromFavorites(recipeId: String): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: FavoriteRecipe): Completable

    @Update
    fun update(recipe: FavoriteRecipe): Completable
}
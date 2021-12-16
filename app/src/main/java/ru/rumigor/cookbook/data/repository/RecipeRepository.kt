package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Unit
import ru.rumigor.cookbook.data.model.UploadImage

interface RecipeRepository {
    fun getRecipes(): Observable<List<Recipe>>
    fun getRecipe(recipeID: String): Observable<Recipe>
    fun getCategories(): Observable<List<Category>>
    fun addRecipe(recipe: Recipe): Observable<Recipe>
    fun updateRecipe(recipe: Recipe): Completable
    fun deleteRecipe(recipeId: String): Completable
    fun getIngredients(): Observable<List<Ingredient>>
    fun getUnits(): Observable<List<Unit>>
    fun addIngredient(ingredient: Ingredient): Observable<ServerResponse>

    fun loadFavorites(userId: String): Observable<List<Recipe>>
    fun loadFavoriteRecipe(recipeId: String): Observable<FavoriteRecipe>
    fun addToFavorites(userId: String, recipeId: String): Completable

    fun favoriteSearch(userId: String, name: String): Observable<List<Recipe>>

    fun deleteFromFavorites(userId: String, recipeId: String): Completable

    fun getRecipesByCategory(categoryId: String): Observable<List<Recipe>>

    fun findRecipeByName(title: String): Observable<List<Recipe>>

    fun findRecipeByName(categoryId: String, title: String): Observable<List<Recipe>>

    fun getTags(): Observable<List<Tag>>

    fun getImages(recipeId: String): Observable<List<RecipeImages>>

    fun addImage(recipeId: String, image: UploadImage): Completable

    fun addStepImage(recipeId: String, stepNumber: String, image: UploadImage): Completable

    fun deleteImage(recipeId: String, fileKey: String): Completable

    fun removeStepImage(recipeId: String, stepNumber: String, fileKey: String): Completable

    fun getStepImages(recipeId: String): Observable<Map<String, List<RecipeImages>>>

    fun getRecipeTags(recipeId: String): Observable<List<Tag>>

    fun getUsers(): Observable<List<User>>

    fun getUser(userId: String): Observable<User>

    fun addTagToRecipe(recipeId: String, tagId: String): Completable

    fun removeTagFromRecipe(recipeId: String, tagId: String): Completable



}
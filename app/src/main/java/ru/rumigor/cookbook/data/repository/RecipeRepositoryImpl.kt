package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import ru.rumigor.cookbook.data.api.CookbookApi
import ru.rumigor.cookbook.data.di.modules.InMemory
import ru.rumigor.cookbook.data.di.modules.Persisted
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Unit
import ru.rumigor.cookbook.data.model.UploadImage
import ru.rumigor.cookbook.data.storage.CookbookDatabase
import java.io.File
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val cookbookApi: CookbookApi,
    @Persisted private val cookbookDatabase: CookbookDatabase
) : RecipeRepository {

    override fun getRecipes(): Observable<List<Recipe>> =
        cookbookApi
            .getRecipes()
            .map { it.recipes }
            .toObservable()

    override fun getRecipe(recipeID: String): Observable<Recipe> =
        cookbookApi
            .getRecipe(recipeID)
            .toObservable()

    override fun getCategories(): Observable<List<Category>> =
        cookbookApi
            .getCategory()
            .toObservable()


    override fun addRecipe(recipe: Recipe): Observable<Recipe> =
        cookbookApi
            .addRecipe(recipe)
            .toObservable()

    override fun updateRecipe(recipe: Recipe): Completable =
        cookbookApi
            .updateRecipe(recipe.id, recipe)

    override fun deleteRecipe(recipeId: String): Completable =
        cookbookApi
            .deleteRecipe(recipeId)

    override fun getIngredients(): Observable<List<Ingredient>> =
        cookbookApi
            .getIngredients()
            .toObservable()

    override fun getUnits(): Observable<List<Unit>> =
        cookbookApi
            .getUnits()
            .toObservable()

    override fun addIngredient(ingredient: Ingredient): Observable<ServerResponse> =
        cookbookApi
            .addIngredient(ingredient = ingredient)
            .toObservable()

    override fun loadFavorites(): Observable<List<FavoriteRecipe>> =
        cookbookDatabase
            .cookbookDao()
            .loadFavoritesRecipes()

    override fun loadFavoriteRecipe(recipeId: String): Observable<FavoriteRecipe> =
        cookbookDatabase
            .cookbookDao()
            .loadFavoriteRecipe(recipeId)
            .toObservable()

    override fun addToFavorites(recipe: FavoriteRecipe): Single<FavoriteRecipe> =
        cookbookDatabase
            .cookbookDao()
            .insert(recipe)
            .andThen(Single.just(recipe))

    override fun favoriteSearch(name: String): Observable<List<FavoriteRecipe>> =
        cookbookDatabase
            .cookbookDao()
            .favoriteSearch(name)

    override fun deleteFromFavorites(recipeId: String): Completable =
        cookbookDatabase
            .cookbookDao()
            .deleteFromFavorites(recipeId)

    override fun getRecipesByCategory(categoryId: String): Observable<List<Recipe>> =
        cookbookApi
            .getRecipesByCategory(categoryId)
            .map { it.recipes }
            .toObservable()

    override fun findRecipeByName(title: String): Observable<List<Recipe>> =
        cookbookApi
            .findRecipeByName(title)
            .map { it.recipes }
            .toObservable()

    override fun findRecipeByName(categoryId: String, title: String): Observable<List<Recipe>> =
        cookbookApi
            .findRecipeByNameInCategory(categoryId, title)
            .map { it.recipes }
            .toObservable()

    override fun getTags(): Observable<List<Tag>> =
        cookbookApi
            .getTags()
            .toObservable()

    override fun getImages(recipeId: String): Observable<List<RecipeImages>> =
        cookbookApi
            .getImage(recipeId)
            .map { it.files }
            .toObservable()

    override fun addImage(
        recipeId: String,
        image: UploadImage
    ): Completable =
        cookbookApi
            .addImage(recipeId, image)

    override fun deleteImage(recipeId: String, fileKey: String): Completable =
        cookbookApi
            .removeImage(recipeId, fileKey)
}
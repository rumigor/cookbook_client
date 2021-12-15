package ru.rumigor.cookbook.data.repository

import androidx.room.PrimaryKey
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

    override fun loadFavorites(userId: String): Observable<List<Recipe>> =
        cookbookApi
            .getFavorites(userId)
            .toObservable()

    override fun loadFavoriteRecipe(recipeId: String): Observable<FavoriteRecipe> =
        cookbookDatabase
            .cookbookDao()
            .loadFavoriteRecipe(recipeId)
            .toObservable()

    override fun addToFavorites(userId: String, recipeId: String): Completable =
        cookbookApi
            .addToFavorites(userId, recipeId)

    override fun favoriteSearch(userId: String, name: String): Observable<List<Recipe>> =
        cookbookApi
            .findInFavorites(userId, name)
            .toObservable()

    override fun deleteFromFavorites(userId: String, recipeId: String): Completable =
        cookbookApi
            .deleteFromFavorites(userId, recipeId)

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

    override fun addStepImage(
        recipeId: String,
        stepNumber: String,
        image: UploadImage
    ): Completable =
        cookbookApi
            .addStepImage(recipeId, stepNumber, image)


    override fun deleteImage(recipeId: String, fileKey: String): Completable =
        cookbookApi
            .removeImage(recipeId, fileKey)

    override fun removeStepImage(
        recipeId: String,
        stepNumber: String,
        fileKey: String
    ): Completable =
        cookbookApi
            .removeStepImage(stepNumber, recipeId, fileKey)

    override fun getStepImages(recipeId: String): Observable<Map<String, List<RecipeImages>>> =
        cookbookApi
            .getImage(recipeId)
            .map { it.stepImages }
            .toObservable()

    override fun getRecipeTags(recipeId: String): Observable<List<Tag>> =
        cookbookApi
            .getRecipeTags(recipeId)
            .map{it.tags}
            .toObservable()

    override fun getUsers(): Observable<List<User>> =
        cookbookApi
            .getUsers()
            .toObservable()

    override fun getUser(userId: String): Observable<User> =
        cookbookApi
            .getUser(userId)
            .toObservable()
}

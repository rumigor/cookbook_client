package ru.rumigor.cookbook.data.api

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Tag
import ru.rumigor.cookbook.data.model.Unit

interface CookbookApi {

    @GET("/api/v1/recipe")
    fun getRecipes(): Single<Content>
    @GET("/api/v1/recipe/{recipe_id}")
    fun getRecipe(@Path("recipe_id") id: String): Single<Recipe>
    @POST("/api/v1/recipe")
    fun addRecipe(@Body recipe: Recipe): Single<Recipe>
    @GET("/api/v1/category")
    fun getCategory(): Single<List<Category>>
    @PUT("/api/v1/recipe/{recipe_id}")
    fun updateRecipe(@Path("recipe_id")id: String, @Body recipe: Recipe): Completable
    @DELETE("/api/v1/recipe/{recipe_id}")
    fun deleteRecipe(@Path("recipe_id") id: String): Completable
    @GET("/api/v1/ingredient")
    fun getIngredients(): Single<List<Ingredient>>
    @GET("/api/v1/unit")
    fun getUnits(): Single<List<Unit>>
    @POST("/api/v1/ingredient")
    fun addIngredient(@Body ingredient: Ingredient): Single<ServerResponse>
    @GET("/api/v1/recipe")
    fun getRecipesByCategory(@Query("categoryId")categoryId: String): Single<Content>
    @GET("/api/v1/recipe")
    fun findRecipeByName(@Query("name")title: String): Single<Content>
    @GET("/api/v1/recipe")
    fun findRecipeByNameInCategory(
        @Query("categoryId") categoryId: String,
        @Query("name")title: String): Single<Content>
    @GET("/api/v1/tag")
    fun getTags(): Single<List<Tag>>
    @GET("/api/v1/recipe/{recipe_id}/image")
    fun getImage(@Path("recipe_id")recipeId: String): Single<Image>


}
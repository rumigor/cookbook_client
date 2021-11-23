package ru.rumigor.cookbook.data.api

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*
import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe
import ru.rumigor.cookbook.data.model.ServerResponse

interface CookbookApi {

    @GET("/cookbook/rest/recipe")
    fun getRecipes(): Single<List<Recipe>>
    @GET("/cookbook/rest/recipe/{recipe_id}")
    fun getRecipe(@Path("recipe_id") id: String): Single<Recipe>
    @POST("/cookbook/rest/recipe")
    fun addRecipe(@Body recipe: Recipe): Single<ServerResponse>
    @GET("/cookbook/rest/category")
    fun getCategory(): Single<List<Category>>
    @PUT("/cookbook/rest/recipe")
    fun updateRecipe(@Body recipe: Recipe): Single<ServerResponse>
    @DELETE("/cookbook/rest/recipe/{recipe_id}")
    fun deleteRecipe(@Path("recipe_id") id: String): Single<ServerResponse>


}
package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Unit

interface RecipeRepository {
    fun getRecipes(): Observable<List<Recipe>>
    fun getRecipe(recipeID: String): Observable<Recipe>
    fun getCategories(): Observable<List<Category>>
    fun addRecipe(recipe: Recipe): Observable<ServerResponse>
    fun updateRecipe(recipe: Recipe): Observable<ServerResponse>
    fun deleteRecipe(recipeId: String): Observable<ServerResponse>
    fun getIngredients(): Observable<List<Ingredient>>
    fun getUnits(): Observable<List<Unit>>
    fun addIngredient(ingredient: Ingredient): Observable<ServerResponse>
}
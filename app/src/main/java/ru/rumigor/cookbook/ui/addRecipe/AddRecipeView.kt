package ru.rumigor.cookbook.ui.addRecipe

import moxy.viewstate.strategy.alias.SingleState
import okhttp3.ResponseBody
import retrofit2.Response
import ru.rumigor.cookbook.ui.*

interface AddRecipeView: ScreenView {
    @SingleState
    fun showCategories(categories: List<CategoryViewModel>)
    @SingleState
    fun showAnswer(recipeViewModel: RecipeViewModel)
    @SingleState
    fun showIngredients(ingredients: List<IngredientsViewModel>)
    @SingleState
    fun showUnits(units: List<UnitViewModel>)
    @SingleState
    fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel)
    @SingleState
    fun loadImage(response: ImageServerResponseViewModel)
    @SingleState
    fun showUpdatedRecipe()
    @SingleState
    fun loadTagsList(tags: List<TagViewModel>)
    @SingleState()
    fun fileUploaded(responseBody: Response<String>)
}
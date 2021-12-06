package ru.rumigor.cookbook.ui.addRecipe

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.*

interface AddRecipeView: ScreenView {
    @SingleState
    fun showCategories(categories: List<CategoryViewModel>)
    @SingleState
    fun showAnswer(serverResponse: ServerResponseViewModel)
    @SingleState
    fun showIngredients(ingredients: List<IngredientsViewModel>)
    @SingleState
    fun showUnits(units: List<UnitViewModel>)
    @SingleState
    fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel)
    @SingleState
    fun loadImage(response: ImageServerResponseViewModel)
}
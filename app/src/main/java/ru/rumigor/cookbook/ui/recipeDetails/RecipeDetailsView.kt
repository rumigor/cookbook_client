package ru.rumigor.cookbook.ui.recipeDetails

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface RecipeDetailsView: ScreenView {
    @SingleState
    fun showRecipe(recipe: RecipeViewModel)
}
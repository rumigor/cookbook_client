package ru.rumigor.cookbook.ui.recipesList

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.data.model.RecipeImages
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface RecipesListView: ScreenView {
    @SingleState
    fun showRecipes(recipes: List<RecipeViewModel>)

}
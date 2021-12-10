package ru.rumigor.cookbook.ui.recipeDetails

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface RecipeDetailsView: ScreenView {
    @AddToEndSingle
    fun showRecipe(recipe: RecipeViewModel)
    @AddToEndSingle
    fun favoriteError(error: Throwable)
    @AddToEndSingle
    fun markFavorite(favoriteRecipe: FavoritesViewModel)
    @AddToEndSingle
    fun onDelete()
    @AddToEndSingle
    fun showImage(images: List<RecipeImagesViewModel>)
}
package ru.rumigor.cookbook.ui.recipeDetails

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState
import moxy.viewstate.strategy.alias.Skip
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.model.RecipeImages
import ru.rumigor.cookbook.ui.*

interface RecipeDetailsView: ScreenView {
    @AddToEndSingle
    fun showRecipe(recipe: RecipeViewModel)
    @Skip
    fun favoriteError(error: Throwable)
    @Skip
    fun markFavorite(favorites: List<RecipeViewModel>)
    @Skip
    fun onDelete()
    @Skip
    fun showImage(images: List<RecipeImagesViewModel>)
    @Skip
    fun loadStepImages(stepImages: Map<String, List<RecipeImages>>)
    @AddToEndSingle
    fun showTags(tags: List<TagViewModel>)
}
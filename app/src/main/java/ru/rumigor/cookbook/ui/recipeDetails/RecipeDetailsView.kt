package ru.rumigor.cookbook.ui.recipeDetails

import moxy.viewstate.strategy.alias.AddToEnd
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
    @AddToEndSingle
    fun onDelete()
    @AddToEndSingle
    fun showImage(images: List<RecipeImagesViewModel>)
    @Skip
    fun loadStepImages(stepImages: Map<String, List<RecipeImages>>)
    @AddToEndSingle
    fun showTags(tags: List<TagViewModel>)
    @AddToEndSingle
    fun showGrade(grade: RatingViewModel)
    @Skip
    fun addGrade()
    @Skip
    fun updateGrade()
    @Skip
    fun deleteGrade()
    @Skip
    fun onGradeGettingError(e: Throwable)

}
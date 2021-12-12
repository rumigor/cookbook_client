package ru.rumigor.cookbook.ui.addRecipe

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState
import moxy.viewstate.strategy.alias.Skip
import okhttp3.ResponseBody
import retrofit2.Response

import ru.rumigor.cookbook.data.model.FileResponse
import ru.rumigor.cookbook.ui.*

interface AddRecipeView: ScreenView {
    @AddToEndSingle
    fun showCategories(categories: List<CategoryViewModel>)
    @SingleState
    fun showAnswer(recipeViewModel: RecipeViewModel)
    @AddToEndSingle
    fun showIngredients(ingredients: List<IngredientsViewModel>)
    @AddToEndSingle
    fun showUnits(units: List<UnitViewModel>)
    @SingleState
    fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel)
    @AddToEndSingle
    fun loadImage(response: ImageServerResponseViewModel)
    @SingleState
    fun showUpdatedRecipe()
    @AddToEndSingle
    fun loadTagsList(tags: List<TagViewModel>)
    @AddToEndSingle
    fun fileUploaded(response: Response<ResponseBody>)
    @Skip
    fun showImage(images: List<RecipeImagesViewModel>)
    @SingleState
    fun addPhoto()
    @Skip
    fun showStepImage(images: List<RecipeImagesViewModel>)
}
package ru.rumigor.cookbook.ui.addRecipe

import moxy.viewstate.strategy.alias.*
import okhttp3.ResponseBody
import retrofit2.Response

import ru.rumigor.cookbook.data.model.FileResponse
import ru.rumigor.cookbook.data.model.RecipeImages
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
    @AddToEndSingle
    fun addPhoto()
    @AddToEndSingle
    fun showStepImage(images: List<RecipeImagesViewModel>)
    @AddToEndSingle
    fun loadStepImages(stepImages: Map<String, List<RecipeImages>>)
    @AddToEndSingle
    fun loadTags(tags: List<TagViewModel>)
    @AddToEndSingle
    fun photoUploading()
}
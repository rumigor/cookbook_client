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
    @Skip
    fun showAnswer(recipeViewModel: RecipeViewModel)
    @AddToEndSingle
    fun showIngredients(ingredients: List<IngredientsViewModel>)
    @AddToEndSingle
    fun showUnits(units: List<UnitViewModel>)
    @Skip
    fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel)
    @Skip
    fun loadImage(response: ImageServerResponseViewModel)
    @Skip
    fun showUpdatedRecipe()
    @AddToEndSingle
    fun loadTagsList(tags: List<TagViewModel>)
    @Skip
    fun fileUploaded(response: Response<ResponseBody>)
    @Skip
    fun showImage(images: List<RecipeImagesViewModel>)
    @Skip
    fun addPhoto()
    @Skip
    fun showStepImage(images: List<RecipeImagesViewModel>)
    @Skip
    fun loadStepImages(stepImages: Map<String, List<RecipeImages>>)
    @Skip
    fun loadTags(tags: List<TagViewModel>)
    @Skip
    fun photoUploading()
    @Skip
    fun success()
}
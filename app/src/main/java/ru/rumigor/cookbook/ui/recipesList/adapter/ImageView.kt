package ru.rumigor.cookbook.ui.recipesList.adapter

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface ImageView: ScreenView {
    @SingleState
    fun showImage(images: List<RecipeImagesViewModel>)
}
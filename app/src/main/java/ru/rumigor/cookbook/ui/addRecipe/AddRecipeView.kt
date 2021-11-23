package ru.rumigor.cookbook.ui.addRecipe

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.ScreenView
import ru.rumigor.cookbook.ui.ServerResponseViewModel

interface AddRecipeView: ScreenView {
    @SingleState
    fun showCategories(categories: List<CategoryViewModel>)
    @SingleState
    fun showAnswer(serverResponse: ServerResponseViewModel)
}
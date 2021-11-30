package ru.rumigor.cookbook.ui.category

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface CategoryView: ScreenView {
    @SingleState
    fun showCategories(categories: List<CategoryViewModel>)
}
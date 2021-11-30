package ru.rumigor.cookbook.ui.favorites

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.ScreenView

interface FavoritesView: ScreenView {
    @SingleState
    fun showRecipes(recipes: List<FavoritesViewModel>)

}
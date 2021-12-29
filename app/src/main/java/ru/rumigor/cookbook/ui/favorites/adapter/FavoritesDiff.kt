package ru.rumigor.cookbook.ui.favorites.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel

object FavoritesDiff: DiffUtil.ItemCallback<RecipeViewModel>() {
    private val payload = Any()
    override fun areItemsTheSame(oldItem: RecipeViewModel, newItem: RecipeViewModel): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RecipeViewModel, newItem: RecipeViewModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: RecipeViewModel, newItem: RecipeViewModel) = payload
}
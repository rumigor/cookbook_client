package ru.rumigor.cookbook.ui.favorites.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.FavoritesViewModel

object FavoritesDiff: DiffUtil.ItemCallback<FavoritesViewModel>() {
    private val payload = Any()
    override fun areItemsTheSame(oldItem: FavoritesViewModel, newItem: FavoritesViewModel): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: FavoritesViewModel, newItem: FavoritesViewModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: FavoritesViewModel, newItem: FavoritesViewModel) = payload
}
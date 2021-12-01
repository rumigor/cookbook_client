package ru.rumigor.cookbook.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel


class FavoritesAdapter (private val delegate: Delegate): ListAdapter<FavoritesViewModel, FavoritesHolder>(
    FavoritesDiff
) {

    interface Delegate{
        fun onRecipePicked(recipe: FavoritesViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder =
        FavoritesHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recipe_view, parent, false)
        )

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        holder.bind(getItem(position), delegate = delegate)
    }


}
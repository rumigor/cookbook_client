package ru.rumigor.cookbook.ui.favorites.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rumigor.cookbook.click
import ru.rumigor.cookbook.databinding.RecipeViewBinding
import ru.rumigor.cookbook.setStartDrawableCircleImageFromUri
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel


class FavoritesHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val viewBinding: RecipeViewBinding by viewBinding()

    fun bind(recipe: FavoritesViewModel, delegate: FavoritesAdapter.Delegate?){
        with(viewBinding){
            viewBinding.recipeName.setStartDrawableCircleImageFromUri(recipe.imagePath)
            viewBinding.recipeName.text = recipe.title
            viewBinding.description.text = recipe.description
            viewBinding.category.text = recipe.category

            root.click { delegate?.onRecipePicked(recipe) }
        }
    }

}
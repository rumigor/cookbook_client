package ru.rumigor.cookbook.ui.recipesList.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.rumigor.cookbook.databinding.RecipeViewBinding
import ru.rumigor.cookbook.ui.RecipeViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rumigor.cookbook.click
import ru.rumigor.cookbook.setStartDrawableCircleImageFromUri
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeAdapter

class RecipeHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val viewBinding: RecipeViewBinding by viewBinding()

    fun bind(recipe: RecipeViewModel, delegate: RecipeAdapter.Delegate?){
        with(viewBinding){
            viewBinding.recipeName.setStartDrawableCircleImageFromUri(recipe.imagePath)
            viewBinding.recipeName.text = recipe.title
            viewBinding.description.text = recipe.description
            viewBinding.category.text = recipe.category.title

            root.click { delegate?.onRecipePicked(recipe) }
        }
    }

}
package ru.rumigor.cookbook.ui.favorites.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.model.GlideUrl
import ru.rumigor.cookbook.click
import ru.rumigor.cookbook.databinding.RecipeViewBinding
import ru.rumigor.cookbook.getAuth
import ru.rumigor.cookbook.setStartDrawableCircleImageFromUri
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel


class FavoritesHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val viewBinding: RecipeViewBinding by viewBinding()

    fun bind(recipe: RecipeViewModel, delegate: FavoritesAdapter.Delegate?){
        with(viewBinding){
            recipe.imagePath?.let{url ->
                viewBinding.recipeName.setStartDrawableCircleImageFromUri(url)}
            viewBinding.recipeName.text = recipe.title
            viewBinding.description.text = recipe.description
            viewBinding.category.text = recipe.category.title
            recipe.rank?.let {
                viewBinding.rank.text = it.averageRating.toString()
            }?: run{
                viewBinding.rank.text = "N/A"
            }
            root.click { delegate?.onRecipePicked(recipe) }
        }
    }

}
package ru.rumigor.cookbook.ui.recipeDetails.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import ru.rumigor.cookbook.click
import ru.rumigor.cookbook.databinding.DishimageviewBinding
import ru.rumigor.cookbook.databinding.RecipeViewBinding
import ru.rumigor.cookbook.setStartDrawableCircleImageFromUri
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeAdapter

class ImagesViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val viewBinding: DishimageviewBinding  by viewBinding()



    fun bind(image: RecipeImagesViewModel, delegate: ImagesAdapter.Delegate?){
        Glide.with(viewBinding.root)
            .load(image.url)
            .into(viewBinding.recipeImage)


//            root.click { delegate?.onRecipePicked(recipe) }
        }
    }
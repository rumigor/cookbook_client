package ru.rumigor.cookbook.ui.recipeDetails.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel

object ImagesDiff : DiffUtil.ItemCallback<RecipeImagesViewModel>() {
    private val payload = Any()
    override fun areItemsTheSame(oldItem: RecipeImagesViewModel, newItem: RecipeImagesViewModel): Boolean {
        return oldItem.url == newItem.url
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RecipeImagesViewModel, newItem: RecipeImagesViewModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: RecipeImagesViewModel, newItem: RecipeImagesViewModel) = payload
}
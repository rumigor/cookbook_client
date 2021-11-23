package ru.rumigor.cookbook.ui.recipesList.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.RecipeViewModel

object RecipeDiff:DiffUtil.ItemCallback<RecipeViewModel>() {
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
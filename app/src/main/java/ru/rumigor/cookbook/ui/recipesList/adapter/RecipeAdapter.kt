package ru.rumigor.cookbook.ui.recipesList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeDiff
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeHolder

class RecipeAdapter(private val delegate: Delegate): ListAdapter<RecipeViewModel, RecipeHolder>(
    RecipeDiff
) {

    interface Delegate{
        fun onRecipePicked(recipe: RecipeViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder =
        RecipeHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recipe_view, parent, false)
        )

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.bind(getItem(position), delegate = delegate)
    }


}
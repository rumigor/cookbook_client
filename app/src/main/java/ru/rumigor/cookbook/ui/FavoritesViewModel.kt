package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.*
import java.io.Serializable

class FavoritesViewModel(
    val recipeId: String,
    val title: String,
    val category: String,
    val description: String,
    val imagePath: String
): Serializable {
    object Mapper{
        fun map(recipe: FavoriteRecipe) =
            FavoritesViewModel(
                recipe.id,
                recipe.title,
                recipe.category,
                recipe.description,
                recipe.imagePath
            )
    }
}
package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.*
import java.io.Serializable


class RecipeViewModel(
    val recipeId: String,
    val category: Category,
    val title: String,
    val description: String,
    val ingredients: Ingredients,
    val steps: Steps,
    val recipe: String,
    val user: User,
    val imagePath: String
): Serializable {
    object Mapper{
        fun map(recipe: Recipe) =
            RecipeViewModel(
                recipe.id,
                recipe.category,
                recipe.title,
                recipe.description,
                recipe.ingredients,
                recipe.steps,
                recipe.recipe,
                recipe.user,
                recipe.imagePath
            )
    }
}
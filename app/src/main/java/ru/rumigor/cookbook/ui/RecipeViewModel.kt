package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.*
import java.io.Serializable


class RecipeViewModel(
    val recipeId: String,
    val category: Category,
    val title: String,
    val description: String,
    val ingredients: List<Ingredients>?,
    val steps: List<Steps>,
    val user: User,
    val tags: List<Tag>,
    val imageUrl: String
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
                recipe.user,
                recipe.tags,
                ""
            )
    }
}
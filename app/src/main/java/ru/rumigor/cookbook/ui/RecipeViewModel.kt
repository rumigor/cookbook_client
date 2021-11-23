package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe
import ru.rumigor.cookbook.data.model.User
import java.io.Serializable


class RecipeViewModel(
    val recipeId: String,
    val category: Category,
    val title: String,
    val description: String,
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
                recipe.recipe,
                recipe.user,
                recipe.imagePath
            )
    }
}
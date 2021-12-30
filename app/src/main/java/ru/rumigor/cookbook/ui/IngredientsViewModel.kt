package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Ingredient
import java.io.Serializable

class IngredientsViewModel(
    val id: Int,
    val briefName: String,
    val name: String,
    val group: Boolean,
    val imagePath: String
): Serializable {
    object Mapper{
        fun map(ingredient: Ingredient) =
            IngredientsViewModel(
                id = ingredient.id,
                briefName = ingredient.briefName,
                name = ingredient.name,
                group = ingredient.group,
                imagePath = ingredient.imagePath
            )
    }
}
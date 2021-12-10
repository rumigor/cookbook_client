package ru.rumigor.cookbook.ui


import ru.rumigor.cookbook.data.model.RecipeImages
import java.io.Serializable

class RecipeImagesViewModel (
    val url: String,
    val description: String
): Serializable {
    object Mapper{
        fun map(recipeImage: RecipeImages) =
            RecipeImagesViewModel(
                url = recipeImage.url,
                description = recipeImage.description
            )
    }
}
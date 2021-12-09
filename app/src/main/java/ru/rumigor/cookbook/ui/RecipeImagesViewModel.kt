package ru.rumigor.cookbook.ui


import ru.rumigor.cookbook.data.model.RecipeImages

class RecipeImagesViewModel (
    val url: String,
    val description: String
) {
    object Mapper{
        fun map(recipeImage: RecipeImages) =
            RecipeImagesViewModel(
                url = recipeImage.url,
                description = recipeImage.description
            )
    }
}
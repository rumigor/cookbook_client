package ru.rumigor.cookbook.ui


import ru.rumigor.cookbook.data.model.Image
import ru.rumigor.cookbook.data.model.RecipeImages
import java.io.Serializable

class StepImageViewModel(
    val stepImages: Map<String, List<RecipeImages>>
) : Serializable {
    object Mapper{
        fun map(stepImage: Image) =
            StepImageViewModel(
                stepImages = stepImage.stepImages
            )
    }
}
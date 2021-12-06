package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.ImageData
import ru.rumigor.cookbook.data.model.ImageServerResponse

class ImageServerResponseViewModel(
    val data: ImageData
) {
    object Mapper{
        fun map(imageServerResponse: ImageServerResponse) =
            ImageServerResponseViewModel(
                data = imageServerResponse.data
            )
    }
}
package ru.rumigor.cookbook.ui


import ru.rumigor.cookbook.data.model.Rating
import java.io.Serializable

class RatingViewModel(
    val rate: Int
) : Serializable {
    object Mapper {
        fun map(rating: Rating) =
            RatingViewModel(
                rate = rating.rate
            )
    }
}
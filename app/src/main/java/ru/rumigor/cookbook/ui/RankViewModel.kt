package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Rank
import ru.rumigor.cookbook.data.model.Rating
import java.io.Serializable

class RankViewModel(
    val ratings: Int,
    val totalRating: Int,
    val averageRating: Float
) : Serializable {
    object Mapper {
        fun map(rank: Rank) =
            RankViewModel(
                ratings = rank.ratings,
                totalRating = rank.totalRating,
                averageRating = rank.averageRating
            )
    }
}
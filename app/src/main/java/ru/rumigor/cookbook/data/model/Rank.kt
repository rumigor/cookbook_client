package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Rank (
    @SerializedName("ratings")
    val ratings: Int,
    @SerializedName("totalRating")
    val totalRating: Int,
    @SerializedName("averageRating")
    val averageRating: Float
        ): Serializable
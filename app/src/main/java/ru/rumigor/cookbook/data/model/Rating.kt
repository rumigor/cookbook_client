package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Rating(
    @SerializedName("rate")
    val rate: Int
): Serializable

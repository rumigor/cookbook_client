package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("rate")
    val rate: Int
)

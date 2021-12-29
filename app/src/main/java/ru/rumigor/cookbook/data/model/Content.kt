package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("content")
    val recipes : List<Recipe>
)

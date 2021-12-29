package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class RecipeImages (
    @SerializedName("fileUri")
    val url: String,
    @SerializedName("description")
    val description: String
        )
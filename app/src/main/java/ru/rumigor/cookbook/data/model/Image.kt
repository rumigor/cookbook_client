package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("files")
    val files: List<RecipeImages>
)

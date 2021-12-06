package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class ImageServerResponse(
    @SerializedName("data")
    val data: ImageData
)
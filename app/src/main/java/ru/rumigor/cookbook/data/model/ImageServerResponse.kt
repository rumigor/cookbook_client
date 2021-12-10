package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageServerResponse(
    @SerializedName("data")
    val data: ImageData
): Serializable
package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class FileResponse(
    @SerializedName("Location")
    val location: String
)

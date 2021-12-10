package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class UploadImage(
    @SerializedName("fileKey")
    val fileKey: String,
    @SerializedName("description")
    val description: String
)

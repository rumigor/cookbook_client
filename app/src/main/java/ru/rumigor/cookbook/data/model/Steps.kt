package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Steps(
    @SerializedName("description")
    var stepDescription: String,
//    @SerializedName("imagePath")
//    var stepImagePath: String
) : Serializable

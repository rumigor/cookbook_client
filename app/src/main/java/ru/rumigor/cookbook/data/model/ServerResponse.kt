package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ServerResponse(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,
    @SerializedName("briefName")
    val briefName: String,
    @SerializedName("name")
    val name: String
)

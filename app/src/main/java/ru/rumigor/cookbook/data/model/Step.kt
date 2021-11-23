package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Step(
    @PrimaryKey
    @SerializedName("description")
    val description: String,
    @ColumnInfo(name = "imagePath")
    @SerializedName("imagePath")
    val title: String
)

package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Unit(
    @PrimaryKey
    @SerializedName("id")
    val id: Int
)

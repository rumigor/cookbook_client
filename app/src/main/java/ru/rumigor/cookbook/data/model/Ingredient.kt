package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Ingredient(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "unit")
    @SerializedName("unit")
    val unit: Unit,
    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    val amount: Int,

)
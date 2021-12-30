package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Ingredients(
    @PrimaryKey
    @SerializedName("ingredient")
    val ingredient: Ingredient,
    @SerializedName("unit")
    val unit: Unit,
    @SerializedName("amount")
    val amount: Int
): Serializable
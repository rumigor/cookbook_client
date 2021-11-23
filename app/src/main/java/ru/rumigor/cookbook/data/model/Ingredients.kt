package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Ingredients (
    @PrimaryKey
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>
        )
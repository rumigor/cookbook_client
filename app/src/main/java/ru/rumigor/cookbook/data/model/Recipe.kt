package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Recipe(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    val category: Category,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String,
    val user: User,
    val ingredients: List<Ingredients>,
    @SerializedName("steps")
    val steps: List<Steps>,
    @ColumnInfo(name = "imagePath")
    @SerializedName("imagePath")
    val imagePath: String
)

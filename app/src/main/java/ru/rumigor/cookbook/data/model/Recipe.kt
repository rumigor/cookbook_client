package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    @SerializedName("id")
    val id: String,
    val category: Category,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    val user: User,
    val ingredients: List<Ingredients>?,
    @SerializedName("steps")
    val steps: List<Steps>,
    @SerializedName("rating")
    val rating: Rank?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("prepareTime")
    val prepareTime: Int,
    @SerializedName("comment")
    val comment: String?
)

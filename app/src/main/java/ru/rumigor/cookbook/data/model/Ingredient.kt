package ru.rumigor.cookbook.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Ingredient(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "briefName")
    @SerializedName("briefName")
    val briefName: String,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "group")
    @SerializedName("group")
    val group: Boolean,
    @ColumnInfo(name = "imagePath")
    @SerializedName("imagePath")
    val imagePath: String


) : Serializable
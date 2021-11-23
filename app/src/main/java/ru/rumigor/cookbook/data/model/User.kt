package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String
)

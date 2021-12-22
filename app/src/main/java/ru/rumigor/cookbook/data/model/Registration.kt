package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("matchingPassword")
    val matchingPassword: String,
    @SerializedName("email")
    val email: String
)

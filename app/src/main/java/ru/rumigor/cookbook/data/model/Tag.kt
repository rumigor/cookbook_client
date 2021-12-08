package ru.rumigor.cookbook.data.model

import java.io.Serializable

data class Tag(
    val id: String,
    val name: String,
    val description: String
): Serializable

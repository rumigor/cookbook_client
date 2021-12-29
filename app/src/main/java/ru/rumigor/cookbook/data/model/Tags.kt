package ru.rumigor.cookbook.data.model

import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName("tags")
    val tags: List<Tag>
)

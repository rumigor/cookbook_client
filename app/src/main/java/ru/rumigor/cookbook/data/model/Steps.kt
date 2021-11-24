package ru.rumigor.cookbook.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Steps(
    @SerializedName("steps")
    var steps: MutableList<Nstep>
)

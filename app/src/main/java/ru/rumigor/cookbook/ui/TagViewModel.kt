package ru.rumigor.cookbook.ui

import android.os.Parcelable
import ru.rumigor.cookbook.data.model.Tag
import java.io.Serializable

class TagViewModel (
    val id: String,
    val briefName: String,
    val description: String,
    var isChecked: Boolean
): Serializable {
    object Mapper{
        fun map(tag: Tag) =
            TagViewModel(
                id = tag.id,
                briefName = tag.name,
                description = tag.description,
                isChecked = false
            )
    }
}
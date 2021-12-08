package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Tag

class TagViewModel (
    val id: String,
    val briefName: String,
    val description: String
) {
    object Mapper{
        fun map(tag: Tag) =
            TagViewModel(
                id = tag.id,
                briefName = tag.name,
                description = tag.description
            )
    }
}
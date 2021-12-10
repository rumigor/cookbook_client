package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.Ingredient
import ru.rumigor.cookbook.data.model.Unit
import java.io.Serializable

class UnitViewModel (
    val id: Int,
    val briefName: String,
    val name: String
): Serializable {
    object Mapper{
        fun map(unit: Unit) =
            UnitViewModel(
                id = unit.id,
                briefName = unit.briefName,
                name = unit.name
            )
    }
}
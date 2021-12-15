package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.User
import java.io.Serializable

class UserViewModel(
    val id: String,
    val username: String,
    val email: String
): Serializable {
    object Mapper{
        fun map(user: User) =
            UserViewModel(
                user.id,
                user.username,
                user.email
            )
    }
}
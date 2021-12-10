package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.User
import java.io.Serializable

class UserViewModel(
    id: String,
    username: String,
    email: String
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
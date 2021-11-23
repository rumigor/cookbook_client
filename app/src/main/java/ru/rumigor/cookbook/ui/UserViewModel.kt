package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.User

class UserViewModel(
    id: String,
    username: String,
    email: String
) {
    object Mapper{
        fun map(user: User) =
            UserViewModel(
                user.id,
                user.username,
                user.email
            )
    }
}
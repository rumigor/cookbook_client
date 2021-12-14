package ru.rumigor.cookbook.ui.ui.login

import moxy.viewstate.strategy.alias.SingleState
import ru.rumigor.cookbook.ui.ScreenView
import ru.rumigor.cookbook.ui.UserViewModel

interface LoginView: ScreenView {
    @SingleState
    fun login(users: List<UserViewModel>)
}
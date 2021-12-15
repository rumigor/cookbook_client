package ru.rumigor.cookbook.ui

import moxy.viewstate.strategy.alias.SingleState

interface MainView: ScreenView {
    @SingleState
    fun getUser(user: UserViewModel)
}
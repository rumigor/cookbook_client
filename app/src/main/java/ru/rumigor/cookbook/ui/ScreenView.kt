package ru.rumigor.cookbook.ui

import moxy.MvpView
import moxy.viewstate.strategy.alias.SingleState

interface ScreenView : MvpView {

    @SingleState
    fun showError(error: Throwable)

}
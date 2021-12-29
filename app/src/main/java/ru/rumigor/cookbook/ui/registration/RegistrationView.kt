package ru.rumigor.cookbook.ui.registration

import moxy.viewstate.strategy.alias.Skip
import ru.rumigor.cookbook.ui.ScreenView

interface RegistrationView: ScreenView {
    @Skip
    fun onSuccess()
}
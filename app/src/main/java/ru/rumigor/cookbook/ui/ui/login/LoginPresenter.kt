package ru.rumigor.cookbook.ui.ui.login

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.UserViewModel

class LoginPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
    private val preferences: AppPreferences
) : MvpPresenter<LoginView>() {
    private val disposables = CompositeDisposable()

    fun logon() {
        disposables +=
            recipeRepository
                .getUsers()
                .map { users -> users.map(UserViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::login,
                    viewState::showError
                )
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
package ru.rumigor.cookbook.ui.registration

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers

class RegistrationPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
) : MvpPresenter<RegistrationView>() {

    private val disposable = CompositeDisposable()

    override fun onDestroy() {
        disposable.clear()
    }

    fun registration(username: String, password: String, email: String){
        disposable +=
            recipeRepository
                .registration(username, password, email)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::onSuccess,
                    viewState::showError
                )
    }
}
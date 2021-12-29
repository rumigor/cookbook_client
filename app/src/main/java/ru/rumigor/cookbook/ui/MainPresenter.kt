package ru.rumigor.cookbook.ui

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers

class MainPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
): MvpPresenter<MainView>() {
    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        AppPreferences.userId?.let{ userId ->
            disposables +=
                recipeRepository
                    .getUser(userId)
                    .map (UserViewModel.Mapper::map)
                    .observeOn(schedulers.main())
                    .subscribeOn(schedulers.background())
                    .subscribe(
                        viewState::getUser,
                        viewState::showError
                    )
        }?: kotlin.run { disposables +=
            recipeRepository
                .getUser("1")
                .map (UserViewModel.Mapper::map)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::getUser,
                    viewState::showError
                ) }
    }
}
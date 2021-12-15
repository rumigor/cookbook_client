package ru.rumigor.cookbook.ui.favorites

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel

class FavoritesPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers
): MvpPresenter<FavoritesView>() {
    private val disposables = CompositeDisposable()
    private val userId = AppPreferences.userId!!

    override fun onFirstViewAttach() {

        disposables +=
            recipeRepository
                .loadFavorites(userId)
                .map { recipes-> recipes.map(RecipeViewModel.Mapper::map)}
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipes,
                    viewState::showError
                )
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    fun filterFavorites(query: String){
        disposables +=
            recipeRepository
                .favoriteSearch(userId, query)
                .map { recipes-> recipes.map(RecipeViewModel.Mapper::map)}
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipes,
                    viewState::showError
                )
    }
}
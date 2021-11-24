package ru.rumigor.cookbook.ui.recipesList

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel


class RecipesListPresenter (
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers
        ): MvpPresenter<RecipesListView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getRecipes()
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

    fun displayRecipe(recipeId: String){

    }




}
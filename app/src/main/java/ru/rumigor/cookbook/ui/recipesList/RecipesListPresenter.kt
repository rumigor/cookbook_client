package ru.rumigor.cookbook.ui.recipesList

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.StepImageViewModel
import ru.rumigor.cookbook.ui.recipesList.RecipesListView


class RecipesListPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
    private val query: String?,
    private val categoryId: String?,
    private val topRanked: String?,
    private val quickRecipes: String?,
    private val tagFilter: String?
) : MvpPresenter<RecipesListView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        loadRecipes()
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    fun search(query: String?) {
        query?.let {
            categoryId?.let {
                if (categoryId != "") {
                    disposables +=
                        recipeRepository
                            .findRecipeByName(categoryId, query)
                            .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                            .observeOn(schedulers.main())
                            .subscribeOn(schedulers.background())
                            .subscribe(
                                viewState::showRecipes,
                                viewState::showError
                            )
                } else findRecipe(query)
            } ?: run {
                findRecipe(query)
            }
        }
    }

    private fun findRecipe(query: String) {
        disposables +=
            recipeRepository
                .findRecipeByName(query)
                .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipes,
                    viewState::showError
                )
    }

    fun loadRecipes() {
        when {
            categoryId != "" -> {
                disposables +=
                    recipeRepository
                        .getRecipesByCategory(categoryId!!)
                        .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe(
                            viewState::showRecipes,
                            viewState::showError
                        )
            }
            quickRecipes != "" -> {
                disposables +=
                    recipeRepository
                        .loadQuickestRecipes(30)
                        .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe(
                            viewState::showRecipes,
                            viewState::showError
                        )
            }
            tagFilter != "" -> {
                disposables +=
                    recipeRepository
                        .findRecipesByTags(tagFilter!!)
                        .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe(
                            viewState::showRecipes,
                            viewState::showError
                        )
            }
            else -> {
                disposables +=
                    recipeRepository
                        .getRecipes()
                        .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe(
                            viewState::showRecipes,
                            viewState::showError
                        )
            }
        }
    }
}





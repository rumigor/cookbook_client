package ru.rumigor.cookbook.ui.recipesList

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.recipesList.RecipesListView


class RecipesListPresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
    private val query: String?,
    private val categoryId: String?
) : MvpPresenter<RecipesListView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        categoryId?.let {
            if (categoryId != "") {
                disposables +=
                    recipeRepository
                        .getRecipesByCategory(categoryId)
                        .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe(
                            viewState::showRecipes,
                            viewState::showError
                        )
            } else {
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
        } ?: run {
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

    override fun onDestroy() {
        disposables.dispose()
    }

    fun search(query: String?) {
        query?.let {
            categoryId?.let{
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
            } ?: run{
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
    fun getPhoto(recipeId: String){
        disposables +=
            recipeRepository
                .getImages(recipeId)
                .map {images -> images.map(RecipeImagesViewModel.Mapper::map)}
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showImage,
                    viewState::showError
                )
    }

}





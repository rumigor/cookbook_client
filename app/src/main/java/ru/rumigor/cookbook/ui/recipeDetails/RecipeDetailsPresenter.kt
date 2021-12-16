package ru.rumigor.cookbook.ui.recipeDetails

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.*

class RecipeDetailsPresenter(
    private val recipeId: String,
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
) : MvpPresenter<RecipeDetailsView>() {
    private val disposables = CompositeDisposable()

    private val userId = AppPreferences.userId!!

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getRecipe(recipeId)
                .map(RecipeViewModel.Mapper::map)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showRecipe,
                    viewState::showError
                )
        disposables +=
            recipeRepository
                .loadFavorites(userId)
                .map { recipes -> recipes.map(RecipeViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::markFavorite,
                    viewState::showError
                )
        disposables +=
            recipeRepository
                .getImages(recipeId)
                .map { images -> images.map(RecipeImagesViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showImage,
                    viewState::showError
                )
        disposables +=
            recipeRepository
                .getRecipeTags(recipeId)
                .map { tags -> tags.map(TagViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showTags,
                    viewState::showError
                )

    }

    override fun onDestroy() {
        disposables.clear()
    }


    fun deleteRecipe(recipeId: String) {
        disposables +=
            recipeRepository
                .deleteRecipe(recipeId)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::onDelete,
                    viewState::showError
                )
    }


    fun removeFromFavorites() {
        disposables +=
            recipeRepository
                .deleteFromFavorites(userId, recipeId)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
    }

    fun addToFavorites() {
        disposables +=
            recipeRepository
                .addToFavorites(userId, recipeId)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
    }

    fun loadStepImages(recipeId: String) {
        disposables +=
            recipeRepository
                .getStepImages(recipeId)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::loadStepImages,
                    viewState::showError
                )

    }

}
package ru.rumigor.cookbook.ui.recipeDetails

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ServerResponseViewModel

class RecipeDetailsPresenter (
    private val recipeId: String,
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
): MvpPresenter<RecipeDetailsView>() {
    private val disposables = CompositeDisposable()

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
                .loadFavoriteRecipe(recipeId)
                .map (FavoritesViewModel.Mapper::map)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::markFavorite,
                    viewState::favoriteError
                )
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

    override fun onDestroy() {
        disposables.clear()
    }



    fun deleteRecipe(recipeId: String){
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


    fun removeFromFavorites(recipeId: String){
        disposables +=
            recipeRepository
                .deleteFromFavorites(recipeId)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
    }

    fun addToFavorites(recipe: FavoriteRecipe){
        disposables +=
            recipeRepository
                .addToFavorites(recipe)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
    }
    fun loadStepImages(recipeId: String){
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
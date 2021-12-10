package ru.rumigor.cookbook.ui.recipesList.adapter

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeImagesViewModel

class ImagePresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers
): MvpPresenter<ImageView>() {
    private val disposables = CompositeDisposable()

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

    override fun onDestroy() {
        disposables.clear()
    }
}
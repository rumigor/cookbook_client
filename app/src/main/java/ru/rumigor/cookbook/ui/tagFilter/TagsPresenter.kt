package ru.rumigor.cookbook.ui.tagFilter

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

import moxy.MvpPresenter
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.TagViewModel


class TagsPresenter (
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers
        ): MvpPresenter<TagsView>() {
    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        disposables.clear()
    }

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getTags()
                .observeOn(schedulers.background())
                .map{tags -> tags.map(TagViewModel.Mapper::map)}
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::loadTags,
                    viewState::showError
                )
    }
}
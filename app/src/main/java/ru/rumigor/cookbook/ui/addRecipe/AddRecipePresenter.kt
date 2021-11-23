package ru.rumigor.cookbook.ui.addRecipe

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.model.Category
import ru.rumigor.cookbook.data.model.Recipe
import ru.rumigor.cookbook.data.model.User
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.ServerResponseViewModel

class AddRecipePresenter(private val recipeRepository: RecipeRepository,
                         private val schedulers: Schedulers,
                         private val recipeId: String
): MvpPresenter<AddRecipeView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        disposables+=
           recipeRepository
               .getCategories()
               .map{categories -> categories.map(CategoryViewModel.Mapper::map)}
               .observeOn(schedulers.main())
               .subscribeOn(schedulers.background())
               .subscribe(
                   viewState::showCategories,
                   viewState::showError
               )
    }

    fun saveRecipe(title: String, description: String, recipe: String, imagePath: String, categoryId : Int) {
        if (recipeId == "0") {
            disposables +=
                recipeRepository
                    .addRecipe(
                        Recipe(
                            "0",
                            Category(categoryId, ""),
                            description = description,
                            recipe = recipe,
                            imagePath = imagePath,
                            title = title,
                            user = User("4", "", "")
                        )
                    )
                    .map(ServerResponseViewModel.Mapper::map)
                    .observeOn(schedulers.main())
                    .subscribeOn(schedulers.background())
                    .subscribe(
                        viewState::showAnswer,
                        viewState::showError
                    )
        } else {
            disposables +=
                recipeRepository
                    .updateRecipe(
                        Recipe(
                            recipeId,
                            Category(categoryId, ""),
                            description = description,
                            recipe = recipe,
                            imagePath = imagePath,
                            title = title,
                            user = User("4", "", "")
                        )
                    )
                    .map(ServerResponseViewModel.Mapper::map)
                    .observeOn(schedulers.main())
                    .subscribeOn(schedulers.background())
                    .subscribe(
                        viewState::showAnswer,
                        viewState::showError
                    )
        }
    }



    override fun onDestroy() {
        disposables.dispose()
    }
}
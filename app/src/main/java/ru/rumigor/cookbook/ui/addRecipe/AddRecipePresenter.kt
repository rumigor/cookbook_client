package ru.rumigor.cookbook.ui.addRecipe

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.UploadImage
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.*

class AddRecipePresenter(
    private val recipeRepository: RecipeRepository,
    private val schedulers: Schedulers,
    private val uploadImage: UploadImage
) : MvpPresenter<AddRecipeView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        disposables +=
            recipeRepository
                .getIngredients()
                .map { ingredients -> ingredients.map(IngredientsViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showIngredients,
                    viewState::showError
                )

        disposables +=
            recipeRepository
                .getCategories()
                .map { categories -> categories.map(CategoryViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showCategories,
                    viewState::showError
                )

        disposables +=
            recipeRepository
                .getUnits()
                .map { units -> units.map(UnitViewModel.Mapper::map) }
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe(
                    viewState::showUnits,
                    viewState::showError
                )
    }

    fun saveRecipe(
        recipeId: String,
        title: String,
        description: String,
//        imagePath: String,
        categoryId: Int,
        ingredients: List<Ingredients>,
        steps: List<Steps>
    ) {
        if (recipeId == "0") {
            disposables +=
                recipeRepository
                    .addRecipe(
                        Recipe(
                            "0",
                            Category(categoryId, ""),
                            description = description,
//                            imagePath = imagePath,
                            title = title,
                            user = User("4", "", ""),
                            ingredients = ingredients,
                            steps = steps
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
//                            imagePath = imagePath,
                            title = title,
                            user = User("4", "", ""),
                            ingredients = ingredients,
                            steps = steps
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

    fun addIngredient(ingredient: Ingredient) {
        disposables +=
            recipeRepository
                .addIngredient(ingredient)
                .observeOn(schedulers.main())
                .map(ServerResponseViewModel.Mapper::map)
                .subscribeOn(schedulers.main())
                .subscribe(
                    viewState::addIngredientToServer,
                    viewState::showError
                )
    }

    fun loadImage(file: String?){
        file?.let {
            disposables +=
                uploadImage
                    .uploadImage(file)
                    .observeOn(schedulers.main())
                    .map(ImageServerResponseViewModel.Mapper::map)
                    .subscribeOn(schedulers.background())
                    .subscribe(
                        viewState::loadImage,
                        viewState::showError
                    )
        }
    }
}
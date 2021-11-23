package ru.rumigor.cookbook.ui.addRecipe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.AddrecipeViewBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.ServerResponseViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsPresenter
import javax.inject.Inject

private const val ARG_RECIPE = "RECIPE"

class AddRecipeFragment : AbsFragment(R.layout.addrecipe_view), AddRecipeView {

    private var recipeId = "0"

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    private val presenter: AddRecipePresenter by moxyPresenter {
        AddRecipePresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            recipeId = recipeId
        )
    }

    private val ui: AddrecipeViewBinding by viewBinding()

    private var categoryId = 1

    override fun showCategories(categories: List<CategoryViewModel>) {
        val categoriesList = mutableListOf<String>()
        for (category in categories) {
            categoriesList.add(category.tittle)
        }

        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ui.chooseCategory.adapter = spinnerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recipe = (arguments?.getSerializable(ARG_RECIPE) as RecipeViewModel?)
        recipe?.let{
            ui.newTitle.setText(recipe.title)
            ui.newDescription.setText(recipe.description)
            ui.recipeDetails.setText(recipe.recipe)
            ui.url.setText(recipe.imagePath)
            ui.chooseCategory.setSelection(categoryId)
            ui.addRecipeButton.text = "Изменить рецепт"
        }
        ui.loadImage.setOnClickListener {
            if (ui.url.text.toString() != "") {
                context?.let {
                    Glide.with(it)
                        .load(ui.url.text.toString())
                        .into(ui.imageView)
                }
            }
        }
        ui.addRecipeButton.setOnClickListener {
            if ((ui.newTitle.text.toString() != "") && (ui.newDescription.text.toString() != "")
                    && (ui.recipeDetails.text.toString() != "")){
                val title = ui.newTitle.text.toString()
                val description = ui.newDescription.text.toString()
                val recipe = ui.recipeDetails.text.toString()
                val imagePath = ui.url.text.toString()
                categoryId = ui.chooseCategory.selectedItemPosition + 1
                presenter.saveRecipe(title, description, recipe, imagePath, categoryId)
            }
        }
    }

    override fun showAnswer(serverResponse: ServerResponseViewModel) {
        Toast.makeText(requireContext(), "new id: ${serverResponse.id}", Toast.LENGTH_LONG).show()
        presenter.showRecipe(serverResponse.id)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        Log.d("ERROR", error.message.toString())
    }


}
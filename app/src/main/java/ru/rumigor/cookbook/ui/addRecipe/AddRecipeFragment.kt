package ru.rumigor.cookbook.ui.addRecipe

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.*
import androidx.core.view.marginEnd
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.model.Ingredient
import ru.rumigor.cookbook.data.model.Ingredients
import ru.rumigor.cookbook.data.model.Steps
import ru.rumigor.cookbook.data.model.Unit
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.AddrecipeViewBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.*
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject

private const val ARG_RECIPE = "RECIPE"

class AddRecipeFragment : AbsFragment(R.layout.addrecipe_view), AddRecipeView {

    private var recipeId = "0"

    private var ingredientId = 0

    private var stepIndex = 0

    private var newSteps = mutableListOf<Steps>()

    private var newIngredients = mutableListOf<Ingredients>()

    private var ingredientsList: MutableList<String> = mutableListOf()
    private var ingredientsIds: MutableList<Int> = mutableListOf()
    private var unitsList: MutableList<String> = mutableListOf()
    private var unitsIds: MutableList<Int> = mutableListOf()

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    private val presenter: AddRecipePresenter by moxyPresenter {
        AddRecipePresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
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
        ui.chooseCategory.setSelection(categoryId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recipe = (arguments?.getSerializable(ARG_RECIPE) as RecipeViewModel?)
        recipe?.let {
            recipeId = recipe.recipeId
            ui.newTitle.setText(recipe.title)
            ui.newDescription.setText(recipe.description)
            ui.url.setText(recipe.imagePath)
            categoryId = recipe.category.id - 1
            ui.addRecipeButton.text = "Изменить рецепт"
            newIngredients = recipe.ingredients as MutableList<Ingredients>
            loadSteps(recipe.steps)
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

        ui.addIngredient.setOnClickListener {
            addIngredient()
        }

        ui.addStep.setOnClickListener {
            stepIndex++
            val stepText = EditText(context)
            stepText.hint = "Введите описание этапа"
            val stepNumber = EditText(context)
            val stepImage = EditText(context)
            stepImage.hint = "Вставьте ссылку на фото"
            stepNumber.setText(getString(R.string.stage, stepIndex))
            ui.steps.addView(stepNumber)
            ui.steps.addView(stepText)
            ui.steps.addView(stepImage)
        }
        ui.addRecipeButton.setOnClickListener {
            if ((ui.newTitle.text.toString() != "") && (ui.newDescription.text.toString() != "")
            ) {
                val title = ui.newTitle.text.toString()
                val description = ui.newDescription.text.toString()
                val imagePath = ui.url.text.toString()
                categoryId = ui.chooseCategory.selectedItemPosition + 1
                val newIngredients = mutableListOf<Ingredients>()
                for (k in 0 until ui.ingredients.childCount){
                    val unitName = unitsList[(((ui.ingredients.getChildAt(k)) as TableRow)
                        .getChildAt(2) as Spinner).selectedItemId.toInt()]
                    val newIngredient = Ingredients(
                        Ingredient(getIndex(
                            (((ui.ingredients.getChildAt(k)) as TableRow).getChildAt(0)
                                as TextView).text.toString()
                        ),"",""), Unit(getUnitIndex(unitName), "", ""),
                        (((ui.ingredients.getChildAt(k)
                            as TableRow).getChildAt(1)) as TextView).text.toString().toInt()
                    )
                    newIngredients.add(newIngredient)
                }
                for (i in 0 until ui.steps.childCount step 3) {
                    val newStep = Steps(
                        (ui.steps.getChildAt(i + 1) as EditText).text.toString(),
                        (ui.steps.getChildAt(i + 2) as EditText).text.toString()
                    )
                    newSteps.add(newStep)
                }
                presenter.saveRecipe(recipeId, title, description, imagePath, categoryId, newIngredients, newSteps)
            }
        }
    }

    private fun addIngredient() {
        val ingredientsAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                ingredientsList
            )
        val unitsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitsList)
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val tableRow = TableRow(context)
        tableRow.setPadding(0, 4, 0, 4)
        tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        val ingredientName = AutoCompleteTextView(context)
        ingredientName.setAdapter(ingredientsAdapter)
        ingredientName.hint = "Наименование"
        ingredientName.threshold = 1
        ingredientName.setPadding(0, 0, 6, 0)
        tableRow.addView(ingredientName)
        val ingredientAmount = EditText(context)
        ingredientAmount.hint = "количество"
        ingredientAmount.setPadding(0, 0, 6, 0)
        tableRow.addView(ingredientAmount)
        val ingredientUnit = Spinner(context)
        ingredientUnit.adapter = unitsAdapter
        ingredientUnit.prompt = "мера ингредиента"
        tableRow.addView(ingredientUnit)
        ui.ingredients.addView(tableRow)
    }

    private fun getUnitIndex(unitName: String): Int {
        var unitId = 0
        for ((i, unit) in unitsList.withIndex()){
            if (unitName == unit){
                unitId = unitsIds[i]
                break
            }
        }
        return unitId
    }

    private fun getIndex(name: String): Int {
        ingredientId = 0
        for ((i, ingredient) in ingredientsList.withIndex()){
            if (name == ingredient) {
                ingredientId = ingredientsIds[i]
                break
            }
        }
        if (ingredientId != 0) return ingredientId
        else {
            addNewIngredientToServer(name)
            return ingredientId
        }
    }

    private fun addNewIngredientToServer(name: String): Int {
        val ingredient = Ingredient(0, "", name)
        presenter.addIngredient(ingredient)
        return ingredientId
    }

    private fun loadIngredients(ingredients: List<Ingredients>) {
        val ingredientsAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                ingredientsList
            )
        val unitsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitsList)
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for (ingredient in ingredients) {
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            tableRow.setPadding(0,4,0,4)
            val ingredientName = AutoCompleteTextView(context)
            ingredientName.setAdapter(ingredientsAdapter)
            ingredientName.completionHint = "Наименование ингредиента"
            ingredientName.threshold = 1
            ingredientName.setText(ingredient.ingredient.name)
            ingredientName.setPadding(0, 0, 6, 0)
            tableRow.addView(ingredientName)
            val ingredientAmount = EditText(context)
            ingredientAmount.setText(ingredient.amount.toString())
            ingredientName.setPadding(0, 0, 6, 0)
            tableRow.addView(ingredientAmount)
            val ingredientUnit = Spinner(context)
            ingredientUnit.adapter = unitsAdapter
            var position = 0
            for ((i, unit) in unitsList.withIndex()){
                if (unit == ingredient.unit.name){
                    position = i
                    break
                }
            }
            ingredientUnit.setSelection(position)
            tableRow.addView(ingredientUnit)
            ingredientsIds.add(ingredient.ingredient.id)
            unitsIds.add(ingredient.unit.id)
            ui.ingredients.addView(tableRow)
        }

    }

    private fun loadSteps(steps: List<Steps>) {
        for ((i, step) in steps.withIndex()) {
            val stepText = EditText(context)
            val stepNumber = EditText(context)
            val stepImage = EditText(context)
            stepNumber.setText(getString(R.string.stage, i + 1))
            stepText.setText(step.stepDescription)
            stepImage.setText(step.stepImagePath)
            ui.steps.addView(stepNumber)
            ui.steps.addView(stepText)
            ui.steps.addView(stepImage)
            stepIndex = i + 1
        }

    }

    override fun showAnswer(serverResponse: ServerResponseViewModel) {
        Toast.makeText(requireContext(), "new id: ${serverResponse.id}", Toast.LENGTH_LONG).show()
        val navController = findNavController()
        val bundle = Bundle()
        val navBuilder = NavOptions.Builder()
        val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.recipesListFragment, true).build()
        bundle.putString("RecipeID", serverResponse.id.toString())
        navController.navigate(R.id.recipeDetailsFragment, bundle, navOptions)
    }

    override fun showIngredients(ingredients: List<IngredientsViewModel>) {
        for (ingredient in ingredients) {
            ingredientsList.add(ingredient.name)
            ingredientsIds.add(ingredient.id)
        }
    }

    override fun showUnits(units: List<UnitViewModel>) {
        for (unit in units) {
            unitsList.add(unit.name)
            unitsIds.add(unit.id)
        }
        loadIngredients(newIngredients)
    }

    override fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel) {
        ingredientId = serverResponseViewModel.id.toInt()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}
package ru.rumigor.cookbook.ui.recipeDetails

import android.graphics.Typeface
import android.graphics.Typeface.ITALIC
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import moxy.ktx.moxyPresenter
import org.w3c.dom.Text
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipeFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject

private const val ARG_RECIPE_ID = "RecipeID"

class RecipeDetailsFragment : AbsFragment(R.layout.recipe_fragment), RecipeDetailsView {

    private val recipeId: String by lazy {
        arguments?.getString(ARG_RECIPE_ID).orEmpty()
    }

    private lateinit var recipeEdit: RecipeViewModel

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    @Suppress("unused")
    private val presenter: RecipeDetailsPresenter by moxyPresenter {
        RecipeDetailsPresenter(
            recipeId,
            schedulers = schedulers,
            recipeRepository = recipeRepository,
        )
    }

    private val ui: RecipeFragmentBinding by viewBinding()

    override fun showRecipe(recipe: RecipeViewModel) {
        context?.let {
            Glide.with(it)
                .load(recipe.imagePath)
                .into(ui.dishPic)
        }
        ui.recipeTitle.text = recipe.title
        loadIngredients(recipe)
        loadSteps(recipe)

        ui.stages.setPadding(0, 0, 0, 8)


        ui.authorName.text = getString(R.string.author, recipe.user.username)

        ui.authorEmail.text = getString(R.string.email, recipe.user.email)
        recipeEdit = recipe
    }

    private fun loadSteps(recipe: RecipeViewModel) {
        for ((i,step) in recipe.steps.withIndex()){
            val stepTitle = TextView(context)
            stepTitle.text = getString(R.string.stage, i+1)
            stepTitle.gravity = Gravity.CENTER
            stepTitle.typeface = Typeface.DEFAULT_BOLD
            ui.stages.addView(stepTitle)
            val stepDescription = TextView(context)
            stepDescription.text = step.stepDescription
            stepDescription.setTypeface(null, ITALIC)
            ui.stages.addView(stepDescription)
            val stepImage = ImageView(context)
            ui.stages.addView(stepImage)
            context?.let {
                Glide.with(it)
                    .load(step.stepImagePath)
                    .placeholder(R.drawable.noimage)
                    .into(stepImage)
                    .onLoadFailed(AppCompatResources.getDrawable(requireContext(),R.drawable.newnote))
            }
            stepImage.setPadding(0,0,0,8)
        }

    }

    private fun loadIngredients(recipe: RecipeViewModel) {
        for (ingredient in recipe.ingredients){
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            val ingredientName = TextView(context)
            ingredientName.text = ingredient.ingredient.name
            tableRow.addView(ingredientName, 0)
            val amount = TextView(context)
            amount.text = getString(R.string.ingredient, ingredient.amount, ingredient.unit.briefName)
            tableRow.addView(amount)
            ui.ingredientsList.addView(tableRow)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val dialogListener: OnDialogListener = object : OnDialogListener {
        override fun onDialogDelete() {
            presenter.deleteRecipe(recipeId)
            val navController = findNavController()
            navController.navigate(R.id.recipesListFragment)
        }

        override fun onDialogCancel() {}
    }


    private fun loadDialog() {
        val settingsDialogFragment: Dialog = Dialog.newInstance()
        settingsDialogFragment.setOnDialogListener(dialogListener)
        settingsDialogFragment.show(parentFragmentManager, "dialog_fragment")
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_search).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> loadDialog()
            R.id.action_edit -> {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putSerializable("RECIPE", recipeEdit)
                navController.navigate(R.id.addRecipeFragment, bundle)
            }
        }
        return true
    }
}
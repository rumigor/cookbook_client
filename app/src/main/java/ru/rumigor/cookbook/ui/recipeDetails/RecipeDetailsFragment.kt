package ru.rumigor.cookbook.ui.recipeDetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipeFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject

private const val ARG_RECIPE_ID = "RecipeID"

class RecipeDetailsFragment : AbsFragment(R.layout.recipe_fragment), RecipeDetailsView {




    private val recipeId: String by lazy{
        arguments?.getString(ARG_RECIPE_ID).orEmpty()
    }

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

        ui.recipeDetails.text = recipe.recipe

        ui.authorName.text = "Автор: " + recipe.user.username

        ui.authorEmail.text = "E-mail: " + recipe.user.email

//        ui.fabUpdate.setOnClickListener {
//            val navController = findNavController()
//            val bundle = Bundle()
//            bundle.putSerializable("RECIPE", recipe)
//            navController.navigate(R.id.addRecipeFragment, bundle)
//        }
//
//        ui.fabDelete.setOnClickListener{
//            loadDialog()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val dialogListener: OnDialogListener = object : OnDialogListener {
        override fun onDialogDelete() {
            presenter.deleteRecipe(recipeId)
            presenter.showMainScreen()
        }

        override fun onDialogCancel() {

        }
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


}
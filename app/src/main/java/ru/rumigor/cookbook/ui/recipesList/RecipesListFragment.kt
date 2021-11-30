package ru.rumigor.cookbook.ui.recipesList

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipesFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeAdapter
import javax.inject.Inject

private const val ARG_RECIPE_QUERY = "Query"
private const val ARG_CATEGORY_ID = "CategoryId"

class RecipesListFragment: AbsFragment(R.layout.recipes_fragment), RecipesListView, RecipeAdapter.Delegate {

    private val query: String? by lazy {
        arguments?.getString(ARG_RECIPE_QUERY).orEmpty()
    }

    private val categoryId: String? by lazy {
        arguments?.getString(ARG_CATEGORY_ID).orEmpty()
    }

    private lateinit var navController: NavController

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Suppress("unused")
    private val presenter: RecipesListPresenter by moxyPresenter {
        RecipesListPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            query = query,
            categoryId = categoryId
        )
    }

    private val ui: RecipesFragmentBinding by viewBinding()
    private val recipeAdapter = RecipeAdapter(delegate = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        ui.recipesList.adapter = recipeAdapter

        ui.fab.setOnClickListener {
            navController.navigate(R.id.addRecipeFragment)
        }
    }

    override fun showRecipes(recipes: List<RecipeViewModel>) {
        recipeAdapter.submitList(recipes)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(
            requireContext(),
            "Sorry, something go wrong(",
            Toast.LENGTH_LONG
        ).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onRecipePicked(recipe: RecipeViewModel) {
        val bundle = Bundle()
        bundle.putString("RecipeID", recipe.recipeId)
        navController.navigate(R.id.recipeDetailsFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_delete).isVisible = false
        menu.findItem(R.id.action_edit).isVisible = false
        menu.findItem(R.id.action_favorites).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> { requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
                .open()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
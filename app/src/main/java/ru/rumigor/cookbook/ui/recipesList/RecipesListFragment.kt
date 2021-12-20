package ru.rumigor.cookbook.ui.recipesList

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipesFragmentBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipesList.adapter.RecipeAdapter
import javax.inject.Inject
import androidx.appcompat.app.AppCompatActivity


private const val ARG_RECIPE_QUERY = "Query"
private const val ARG_CATEGORY_ID = "CategoryId"
private const val TOP_RANK = "TOP_RANK"

class RecipesListFragment : AbsFragment(R.layout.recipes_fragment), RecipesListView,
    RecipeAdapter.Delegate {

    private val query: String? by lazy {
        arguments?.getString(ARG_RECIPE_QUERY).orEmpty()
    }

    private val categoryId: String? by lazy {
        arguments?.getString(ARG_CATEGORY_ID).orEmpty()
    }

    private val topRanked: String by lazy {
        arguments?.getString(TOP_RANK).orEmpty()
    }


    private lateinit var navController: NavController

    private var imagesCounter = 0

    private var newRecipes = mutableListOf<RecipeViewModel>()

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
            categoryId = categoryId,
            topRanked = topRanked
        )
    }

    private val ui: RecipesFragmentBinding by viewBinding()
    private val recipeAdapter = RecipeAdapter(delegate = this)

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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
        newRecipes.clear()
        newRecipes.addAll(recipes)
        if (topRanked == TOP_RANK) {
            newRecipes.sortByDescending {
                it.rank?.averageRating
            }
            val rankedRecipes = mutableListOf<RecipeViewModel>()
            var top = 0
            top = if (newRecipes.size > 10) 9
            else newRecipes.size - 1
            for (i in 0..top) {
                rankedRecipes.add(newRecipes[i])
            }
            recipeAdapter.submitList(rankedRecipes)
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "ТОП-10"
        } else recipeAdapter.submitList(recipes)

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
        try {
            val search = menu.findItem(R.id.action_search)
            val searchText = search.actionView as SearchView
            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { presenter.search(query) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
            searchText.setOnCloseListener {
                presenter.search("")
                false
            }
        } catch (e: Throwable) {
            Toast.makeText(
                requireContext(),
                "Sorry, something go with search toolbar",
                Toast.LENGTH_LONG
            ).show()
            Log.d("ERROR", e.message.toString())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                presenter.loadRecipes()
            }
            R.id.action_sortByRank -> {
                newRecipes.sortWith(
                    nullsLast(compareByDescending {
                        it.rank?.averageRating
                    })
                )
                var rankedRecipes = mutableListOf<RecipeViewModel>()
                rankedRecipes.addAll(newRecipes)
                recipeAdapter.submitList(rankedRecipes)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
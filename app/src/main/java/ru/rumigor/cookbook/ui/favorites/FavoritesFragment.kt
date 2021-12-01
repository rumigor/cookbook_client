package ru.rumigor.cookbook.ui.favorites

import android.content.Context
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
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.favorites.adapter.FavoritesAdapter
import javax.inject.Inject




class FavoritesFragment: AbsFragment(R.layout.recipes_fragment), FavoritesView, FavoritesAdapter.Delegate {

    private lateinit var navController: NavController

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Suppress("unused")
    private val presenter: FavoritesPresenter by moxyPresenter {
        FavoritesPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    private val ui: RecipesFragmentBinding by viewBinding()
    private val recipeAdapter = FavoritesAdapter(delegate = this)

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

    override fun showRecipes(recipes: List<FavoritesViewModel>) {
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

    override fun onRecipePicked(recipe: FavoritesViewModel) {
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
                    query?.let { presenter.filterFavorites(query) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
            searchText.setOnCloseListener {
                presenter.filterFavorites("")
                false
            }
        } catch (e: Throwable){
            Toast.makeText(
                requireContext(),
                "Sorry, something go wrong with search toolbar",
                Toast.LENGTH_LONG
            ).show()
            Log.d("ERROR", e.message.toString())
        }

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
package ru.rumigor.cookbook.ui.category

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.CategoryFragmentViewBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.CategoryViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.category.adapter.CategoryAdapter
import javax.inject.Inject

class CategoryFragment: AbsFragment(R.layout.category_fragment_view), CategoryView, CategoryAdapter.Delegate {
    private lateinit var navController: NavController

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Suppress("unused")
    private val presenter: CategoryPresenter by moxyPresenter {
        CategoryPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    private val ui: CategoryFragmentViewBinding by viewBinding()
    private val categoryAdapter = CategoryAdapter(this)

    override fun onResume() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onResume()
    }

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
        ui.categoriesRecyclerView.adapter = categoryAdapter
    }

    override fun showCategories(categories: List<CategoryViewModel>) {
        categoryAdapter.submitList(categories)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(
            requireContext(),
            "Sorry, something go wrong(",
            Toast.LENGTH_LONG
        ).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onCategoryPicked(category: CategoryViewModel) {
        val bundle = Bundle()
        bundle.putString("CategoryId", category.id.toString())
        navController.navigate(R.id.recipesListFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}
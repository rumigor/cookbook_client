package ru.rumigor.cookbook.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.arguments
import ru.rumigor.cookbook.databinding.MainfragmentViewBinding
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.addRecipe.AddRecipePresenter
import javax.inject.Inject

class MainFragment: AbsFragment(R.layout.mainfragment_view), MainView {

    companion object Factory{
        fun newInstance(): Fragment = MainFragment().arguments()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        Log.d("ERROR", error.message.toString())
    }

    private val ui: MainfragmentViewBinding by viewBinding()


    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        ui.allRecipes.setOnClickListener {
            navController.navigate(R.id.recipesListFragment)
        }

    }


}
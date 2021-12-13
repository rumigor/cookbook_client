package ru.rumigor.cookbook.ui.recipeDetails

import android.graphics.Typeface
import android.graphics.Typeface.ITALIC
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.model.FavoriteRecipe
import ru.rumigor.cookbook.data.model.RecipeImages
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.RecipeFragmentBinding
import ru.rumigor.cookbook.dp
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.setStartDrawableCircleImageFromUri
import ru.rumigor.cookbook.ui.FavoritesViewModel
import ru.rumigor.cookbook.ui.RecipeImagesViewModel
import ru.rumigor.cookbook.ui.RecipeViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.recipeDetails.adapter.ImagesAdapter
import javax.inject.Inject

private const val ARG_RECIPE_ID = "RecipeID"

class RecipeDetailsFragment : AbsFragment(R.layout.recipe_fragment), RecipeDetailsView, ImagesAdapter.Delegate {

    private val recipeId: String by lazy {
        arguments?.getString(ARG_RECIPE_ID).orEmpty()
    }

    private lateinit var recipeEdit: RecipeViewModel

    private var favorite = false

    private val imagesAdapter = ImagesAdapter(this)

    private lateinit var favoriteItem: MenuItem

    private var favoriteRecipe = FavoriteRecipe("", "", "", "", "")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.imagesRecycleView.adapter = imagesAdapter
    }

    override fun showRecipe(recipe: RecipeViewModel) {
        favoriteRecipe = FavoriteRecipe(recipe.recipeId, recipe.title, recipe.category.title, recipe.description, "")
        ui.recipeTitle.text = recipe.title
        loadIngredients(recipe)
        loadSteps(recipe)

        ui.stages.setPadding(0, 0, 0, 8)
        if (recipe.tags.isNotEmpty()) {
            val tags = StringBuffer()
            for (tag in recipe.tags) {
                tags.append(tag.name + " ")
            }
            ui.tags.text = getString(R.string.tags, tags)
        } else ui.tags.visibility = View.GONE

        ui.authorName.text = getString(R.string.author, recipe.user.username)

        ui.authorEmail.text = getString(R.string.email, recipe.user.email)
        recipeEdit = recipe
    }

    override fun favoriteError(error: Throwable) {
        favorite = false
    }

    override fun markFavorite(favoriteRecipe: FavoritesViewModel) {
        favorite = true
        favoriteItem.setIcon(R.drawable.ic_baseline_favorite_24)
    }

    override fun onDelete() {
        val navController = findNavController()
        navController.navigate(R.id.recipesListFragment)
    }

    override fun showImage(images: List<RecipeImagesViewModel>) {
        if (images.isNotEmpty()){
            imagesAdapter.submitList(images)
        }
        favoriteRecipe.imagePath = images[0].url
    }

    override fun loadStepImages(stepImages: Map<String, List<RecipeImages>>) {
        val adapterLists = mutableListOf<MutableList<RecipeImagesViewModel>>()
        val imageAdapters = mutableListOf<ImagesAdapter>()
        for (i in 0 until stepImages.size){
            adapterLists.add(mutableListOf())
            val stepUrls = stepImages[i.toString()]
            stepUrls?.let{
                for (image in it){
                    val stepImage = RecipeImagesViewModel(image.url, image.description)
                    adapterLists[i].add(stepImage)
                }
            }
            val stepImagesRecycleView = RecyclerView(requireContext())
            val stepImagesAdapter = ImagesAdapter(this)
            imageAdapters.add(stepImagesAdapter)
            stepImagesRecycleView.adapter = imageAdapters[i]
            (ui.stages.getChildAt((i*3)+2) as CardView).addView(stepImagesRecycleView)
            stepImagesRecycleView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            imageAdapters[i].submitList(adapterLists[i])
        }
    }

    private fun loadSteps(recipe: RecipeViewModel) {
        for ((i,step) in recipe.steps.withIndex()){
            val stepTitle = TextView(context)
            stepTitle.text = getString(R.string.stage, i+1)
            stepTitle.gravity = Gravity.CENTER
            stepTitle.typeface = Typeface.DEFAULT_BOLD
            stepTitle.textSize = 20f
            ui.stages.addView(stepTitle)
            val stepDescription = TextView(context)
            stepDescription.text = step.stepDescription
            stepDescription.textSize = 18f
            stepDescription.setTypeface(null, ITALIC)
            ui.stages.addView(stepDescription)
            val stepImages = CardView(requireContext())
            stepImages.cardElevation = 6f
            stepImages.maxCardElevation = 10f
            stepImages.setPadding(2,2,2,2)
            stepImages.background = ResourcesCompat.getDrawable(resources, R.drawable.card_background, requireActivity().theme)
            ui.stages.addView(stepImages)
            stepImages.setPadding(0,0,0,8)
        }
        presenter.loadStepImages(recipeId)

    }

    private fun loadIngredients(recipe: RecipeViewModel) {
        recipe.ingredients?.let {
            for (ingredient in it) {
                val tableRow = TableRow(context)
                tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                val ingredientName = TextView(context)
                ingredientName.text = ingredient.ingredient.name
                ingredientName.textSize = 16f
                tableRow.addView(ingredientName, 0)
                val amount = TextView(context)
                amount.text =
                    getString(R.string.ingredient, ingredient.amount, ingredient.unit.briefName)
                amount.textSize = 16f
                tableRow.addView(amount)
                ui.ingredientsList.addView(tableRow)
            }
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
        if (favorite) menu.findItem(R.id.action_favorites).setIcon(R.drawable.ic_baseline_favorite_24)
        favoriteItem = menu.findItem(R.id.action_favorites)
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
            R.id.action_favorites -> {
                changeStatus(item)
            }
            android.R.id.home -> {
                val navController = findNavController()
                navController.popBackStack()
            }
        }
        return true
    }

    private fun changeStatus(item: MenuItem) {
        if (favorite) {
            favorite = false
            presenter.removeFromFavorites(recipeId)
            item.setIcon(R.drawable.ic_baseline_favorite_border_24)
        } else {
            favorite = true
            presenter.addToFavorites(favoriteRecipe)
            item.setIcon(R.drawable.ic_baseline_favorite_24)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onImagePicked(image: RecipeImagesViewModel) {

    }
}
package ru.rumigor.cookbook.ui.addRecipe

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import moxy.ktx.moxyPresenter
import okhttp3.ResponseBody
import retrofit2.Response
import ru.rumigor.cookbook.ImageFilePath
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Unit
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.UploadImage
import ru.rumigor.cookbook.databinding.AddrecipeViewBinding
import ru.rumigor.cookbook.dp
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.*
import ru.rumigor.cookbook.ui.abs.AbsFragment
import javax.inject.Inject
import android.widget.LinearLayout
import com.bumptech.glide.load.model.GlideUrl
import ru.rumigor.cookbook.getAuth


private const val ARG_RECIPE = "RECIPE"

class AddRecipeFragment : AbsFragment(R.layout.addrecipe_view), AddRecipeView {

    private var recipeId = "0"

    private var imagePath = ""

    private var oldTags = listOf<TagViewModel>()

    private var lastImages = mutableListOf<RecipeImagesViewModel>()

    private var stepFileKeys = mutableListOf<MutableList<String>>()

    private var stepImagesToRemove = mutableListOf<MutableList<String>>()

    private var imagesToRemove = mutableListOf<String>()

    private var stepIndex = -1

    private lateinit var link: TextView
    private lateinit var image: ImageView
    private var fileKeys = mutableListOf<String>()

    private var ingredientId = 0

    private var ingredientIndex = 0

    private lateinit var uploadRecipeImage: ActivityResultLauncher<Intent>

    private lateinit var uploadStepImage: ActivityResultLauncher<Intent>

    private var selectedImagePath = ""

    private val tagsList = mutableListOf<TagViewModel>()
    private val tagsNameList = mutableListOf<String>()

    private var newSteps = mutableListOf<Steps>()

    private var newIngredients = mutableListOf<Ingredients>()

    private var ingredientsList: MutableList<String> = mutableListOf()
    private var ingredientsIds: MutableList<Int> = mutableListOf()
    private var unitsList: MutableList<String> = mutableListOf()
    private var unitsIds: MutableList<Int> = mutableListOf()
    private var recipeImages = mutableListOf<String>()
    private var photoCount = 0

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var uploadImage: UploadImage


    private val presenter: AddRecipePresenter by moxyPresenter {
        AddRecipePresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            uploadImage = uploadImage
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
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        uploadRecipeImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data.let {
                        Toast.makeText(requireContext(), "Изображение выбрано", Toast.LENGTH_SHORT)
                            .show()
                        val imagePath = ImageFilePath.getPath(requireContext(), it)
                        presenter.loadImage(imagePath)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initClocks()
        val recipe = (arguments?.getSerializable(ARG_RECIPE) as RecipeViewModel?)
        recipe?.let {
            recipeId = it.recipeId
            presenter.getPhoto(recipeId)
            ui.newTitle.setText(it.title)
            ui.newDescription.setText(it.description)
            categoryId = it.category.id - 1
            ui.addRecipeButton.text = "Изменить рецепт"
            newIngredients = it.ingredients as MutableList<Ingredients>
            it.imagePath?.let { url -> imagePath = url }
            it.comment?.let{ comment ->
                ui.inputComment.setText(comment)
            }?: run {
                ui.inputComment.setText("")
            }
            loadSteps(it.steps)
            loadTime(it.prepareTime)
        }

        ui.addIngredient.setOnClickListener {
            addIngredient()
        }

        ui.addStep.setOnClickListener {
            val stepText = EditText(context)
            stepText.hint = "Введите описание этапа"
            val stepNumber = EditText(context)
            val addPhoto = Button(context)
            addPhoto.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.light_blue,
                    requireActivity().theme
                )
            )
            addPhoto.text = "Добавить фото"
            val imageFrame = HorizontalScrollView(requireContext())
            val deleteStep = Button(context)
            deleteStep.text = "Удалить этап"
            ui.steps.addView(stepNumber)
            ui.steps.addView(stepText)
            ui.steps.addView(addPhoto)
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            imageFrame.addView(linearLayout)
            addPhoto.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            stepFileKeys.add(mutableListOf())
            stepImagesToRemove.add(mutableListOf())
            addPhoto.setOnClickListener {
                stepIndex = ui.steps.indexOfChild(it) / 5
                pickImage()
            }

            addPhoto.setPadding(2, 2, 2, 2)
            ui.steps.addView(imageFrame)

            ui.steps.addView(deleteStep)
            stepNumber.setText(
                getString(
                    R.string.stage,
                    (ui.steps.indexOfChild(stepNumber) / 5 + 1)
                )
            )
            deleteStep.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.pink,
                    requireActivity().theme
                )
            )
            deleteStep.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            deleteStep.setOnClickListener {
                deleteStep(stepNumber)
            }
        }

        ui.openImage.setOnClickListener {
            stepIndex = -1
            pickStepImage()
        }

        ui.addRecipeButton.setOnClickListener {
            if ((ui.newTitle.text.toString() != "") && (ui.newDescription.text.toString() != "")
            ) {
                if (ui.ingredients.childCount > 0) {
                    if (checkIngredients()) {
                        addRecipe()
                    }
                } else addRecipeToList()
            }
        }
    }

    private fun initClocks() {
        val adapter =
            ArrayAdapter.createFromResource(requireContext(), R.array.time,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ui.hours.adapter = adapter
        ui.minutes.adapter = adapter
    }

    private fun loadTime(prepareTime: Int) {
        ui.hours.setSelection(prepareTime / 60)
        ui.minutes.setSelection(prepareTime % 60)
    }

    private fun pickStepImage() {
        val getIntent = Intent(ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        uploadRecipeImage.launch(getIntent)
    }

    private fun checkIngredients(): Boolean {
        var isAdded = true
        for (k in ingredientIndex until ui.ingredients.childCount) {
            ingredientId = getIndex(
                (((ui.ingredients.getChildAt(k)) as TableRow).getChildAt(1)
                        as TextView).text.toString()
            )
            if (ingredientId == 0) {
                presenter.addIngredient(
                    Ingredient(
                        0,
                        (((ui.ingredients.getChildAt(k)) as TableRow).getChildAt(1)
                                as TextView).text.toString(),
                        (((ui.ingredients.getChildAt(k)) as TableRow).getChildAt(1)
                                as TextView).text.toString()
                    )
                )
                ingredientIndex = k
                isAdded = false
            }
        }
        return isAdded
    }


    private fun removeTag(newTagline: LinearLayout) {
        ui.tagLayout.removeView(newTagline)
    }

    private fun pickImage() {
        val getIntent = Intent(ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        uploadRecipeImage.launch(getIntent)
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
        tableRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        val removeIngredient = ImageView(context)
        removeIngredient.setImageResource(R.drawable.ic_baseline_cancel_24)
        tableRow.addView(removeIngredient)
        val ingredientName = AutoCompleteTextView(context)
        ingredientName.setAdapter(ingredientsAdapter)
        ingredientName.hint = "Наименование"
        ingredientName.threshold = 1
        ingredientName.width = 200.dp(requireContext())
        tableRow.addView(ingredientName)
        ingredientName.layoutParams.height = TableRow.LayoutParams.WRAP_CONTENT
        val ingredientAmount = EditText(context)
        ingredientAmount.inputType = InputType.TYPE_CLASS_NUMBER
        ingredientAmount.hint = "количество"
        tableRow.addView(ingredientAmount)
        val ingredientUnit = Spinner(context)
        ingredientUnit.adapter = unitsAdapter
        ingredientUnit.prompt = "мера ингредиента"
        tableRow.addView(ingredientUnit)
        ui.ingredients.addView(tableRow)
        removeIngredient.setOnClickListener {
            removeIngredient(tableRow)
        }
    }

    private fun removeIngredient(tableRow: TableRow) {
        ui.ingredients.removeView(tableRow)
    }

    private fun getUnitIndex(unitName: String): Int {
        var unitId = 0
        for ((i, unit) in unitsList.withIndex()) {
            if (unitName == unit) {
                unitId = unitsIds[i]
                break
            }
        }
        return unitId
    }

    private fun getIndex(name: String): Int {
        var id = 0
        for ((i, ingredient) in ingredientsList.withIndex()) {
            if (name == ingredient) {
                id = ingredientsIds[i]
                break
            }
        }
        return id
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
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            val removeIngredient = ImageView(context)
            removeIngredient.setImageResource(R.drawable.ic_baseline_cancel_24)
            tableRow.addView(removeIngredient)
            val ingredientName = AutoCompleteTextView(context)
            ingredientName.setAdapter(ingredientsAdapter)
            ingredientName.completionHint = "Наименование ингредиента"
            ingredientName.threshold = 1
            ingredientName.setText(ingredient.ingredient.name)
            ingredientName.width = 200.dp(requireContext())
            tableRow.addView(ingredientName)
            ingredientName.layoutParams.height = TableRow.LayoutParams.WRAP_CONTENT
            val ingredientAmount = EditText(context)
            ingredientAmount.inputType = InputType.TYPE_CLASS_NUMBER
            ingredientAmount.setText(ingredient.amount.toString())
            tableRow.addView(ingredientAmount)
            val ingredientUnit = Spinner(context)
            ingredientUnit.adapter = unitsAdapter
            var position = 0
            for ((i, unit) in unitsList.withIndex()) {
                if (unit == ingredient.unit.name) {
                    position = i
                    break
                }
            }
            ingredientUnit.setSelection(position)
            tableRow.addView(ingredientUnit)
            unitsIds.add(ingredient.unit.id)

            removeIngredient.setOnClickListener { removeIngredient(tableRow) }
            ui.ingredients.addView(tableRow)
        }

    }

    private fun loadSteps(steps: List<Steps>) {
        for (step in steps) {
            val stepText = EditText(context)
            val stepNumber = EditText(context)
            val deleteStep = Button(context)
            val addPhoto = Button(context)
            addPhoto.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.light_blue,
                    requireActivity().theme
                )
            )
            addPhoto.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            addPhoto.text = "Добавить фото"
            val imageFrame = HorizontalScrollView(requireContext())
            deleteStep.text = "Удалить этап"
            stepText.setText(step.stepDescription)
            ui.steps.addView(stepNumber)
            ui.steps.addView(stepText)
            ui.steps.addView(addPhoto)
            ui.steps.addView(imageFrame)
            val linearLayout = LinearLayout(requireContext())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            imageFrame.addView(linearLayout)
            stepFileKeys.add(mutableListOf())
            stepImagesToRemove.add(mutableListOf())
            addPhoto.setOnClickListener {
                stepIndex = ui.steps.indexOfChild(it) / 5
                pickImage()
            }
            ui.steps.addView(deleteStep)
            stepNumber.setText(
                getString(
                    R.string.stage,
                    (ui.steps.indexOfChild(stepNumber) / 5 + 1)
                )
            )
            deleteStep.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.pink,
                    requireActivity().theme
                )
            )
            deleteStep.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            deleteStep.setOnClickListener {
                deleteStep(stepNumber)
            }
        }
        presenter.getStepPhoto(recipeId)

    }

    private fun deleteStep(view: View?) {
        val index = ui.steps.indexOfChild(view)
        for (k in index until ui.steps.childCount step 5) {
            ((ui.steps.getChildAt(k)) as TextView).text = getString(R.string.stage, k / 5)
        }
        for (k in 0..4) {
            ui.steps.removeViewAt(index)
        }
        stepFileKeys.removeAt(index / 5)
    }

    override fun showAnswer(recipeViewModel: RecipeViewModel) {
        recipeId = recipeViewModel.recipeId
        updateTags()
        Toast.makeText(requireContext(), "new id: ${recipeViewModel.recipeId}", Toast.LENGTH_LONG)
            .show()
        updatePhotos()
    }

    override fun showIngredients(ingredients: List<IngredientsViewModel>) {
        for (ingredient in ingredients) {
            ingredientsList.add(ingredient.name)
            ingredientsIds.add(ingredient.id)
        }
    }

    override fun showUnits(units: List<UnitViewModel>) {
        for (unit in units) {
            unitsList.add(unit.briefName)
            unitsIds.add(unit.id)
        }
        loadIngredients(newIngredients)
    }

    override fun addIngredientToServer(serverResponseViewModel: ServerResponseViewModel) {
        ingredientsIds.add(serverResponseViewModel.id.toInt())
        ingredientsList.add(serverResponseViewModel.name)
        if (checkIngredients()) addRecipe()
    }

    private fun addRecipeToList() {
        if (fileKeys.isNotEmpty()) {
            imagePath =
                "http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/file/+${fileKeys[0]}"
        }
        for (i in 0 until ui.steps.childCount step 5) {
            val newStep = Steps(
                (ui.steps.getChildAt(i + 1) as EditText).text.toString(),
            )
            newSteps.add(newStep)
        }
        val title = ui.newTitle.text.toString()
        val description = ui.newDescription.text.toString()
        val time = (ui.hours.selectedItemId*60 + ui.minutes.selectedItemId).toInt()
        val comment = ui.inputComment.text.toString()

        categoryId = ui.chooseCategory.selectedItemPosition + 1
        presenter.saveRecipe(
            recipeId,
            title,
            description,
            imagePath,
            categoryId,
            newIngredients,
            newSteps,
            time,
            comment
        )
    }

    private fun addRecipe() {
        for (k in 0 until ui.ingredients.childCount) {
            ingredientId = getIndex(
                (((ui.ingredients.getChildAt(k)) as TableRow).getChildAt(1)
                        as TextView).text.toString()
            )
            val unitName = unitsList[(((ui.ingredients.getChildAt(k)) as TableRow)
                .getChildAt(3) as Spinner).selectedItemId.toInt()]
            val newIngredient = Ingredients(
                Ingredient(ingredientId, "", ""), Unit(getUnitIndex(unitName), "", ""),
                (((ui.ingredients.getChildAt(k)
                        as TableRow).getChildAt(2)) as TextView).text.toString().toInt()
            )
            newIngredients.add(newIngredient)
        }
        addRecipeToList()
    }

    override fun loadImage(response: ImageServerResponseViewModel) {
        link.text = response.data.url
        context?.let {
            Glide.with(it)
                .load(GlideUrl(selectedImagePath, getAuth()))
                .into(image)
        }
    }

    override fun showUpdatedRecipe() {
        updateTags()
        updatePhotos()
    }

    private fun updatePhotos() {
        for (step in stepFileKeys) {
            if (step.isEmpty()) {
                step.add("258b7a0b-8441-4c32-88a8-3194771ba86a.png")
            }
        }
        photoCount = fileKeys.size
        for (i in stepFileKeys) {
            photoCount += i.size
        }
        presenter.removePhoto(recipeId, imagesToRemove)
        presenter.removeStepPhoto(recipeId, stepImagesToRemove)
        if (fileKeys.isEmpty()) {
            fileKeys.add("258b7a0b-8441-4c32-88a8-3194771ba86a.png")
        }
        presenter.addPhoto(recipeId, fileKeys)
        if (stepFileKeys.isNotEmpty()) {
            presenter.addStepPhoto(recipeId, stepFileKeys)
        }
    }

    private fun updateTags() {
        for (tag in oldTags) {
            presenter.removeTagFromRecipe(recipeId, tag.id)
        }
        for (i in 0 until ui.tagLayout.childCount) {
            val tagName =
                ((((ui.tagLayout.getChildAt(i) as LinearLayout).getChildAt(0)) as Spinner).selectedItem).toString()
            for (t in tagsList) {
                if (tagName == t.briefName) {
                    presenter.addTagToRecipe(recipeId, t.id)
                    break
                }
            }
        }
    }

    override fun loadTagsList(tags: List<TagViewModel>) {
        tagsList.addAll(tags)
        oldTags = tags
        for (tag in tags) {
            tagsNameList.add(tag.briefName)
        }
        ui.addTag.setOnClickListener {
            addTag()
        }
        if (recipeId != "0") {
            presenter.loadTags(recipeId)
        }
    }

    override fun fileUploaded(response: Response<ResponseBody>) {
        val imageUrl = response.headers()["Location"]
        imageUrl?.let { it ->
            val urlParts = it.split("/").toTypedArray()
            if (stepIndex == -1) {
                if (checkFileKey(urlParts[urlParts.size - 1])) {
                    val newImage = ImageView(context)
                    ui.imagesList.addView(newImage)
                    context?.let { context ->
                        Glide.with(context)
                            .load(GlideUrl(it, getAuth()))
                            .apply(
                                RequestOptions
                                    .centerCropTransform()
                                    .override(80.dp(context))
                            )
                            .into(newImage)
                    }
                    fileKeys.add(urlParts[urlParts.size - 1])
                    newImage.setOnLongClickListener {
                        fileKeys.removeAt(ui.imagesList.indexOfChild(it))
                        ui.imagesList.removeView(it)
                        return@setOnLongClickListener true
                    }
                } else Toast.makeText(requireContext(), "Фото уже добавлено", Toast.LENGTH_LONG)
                    .show()
            } else {
                val newImage = ImageView(context)
                ((ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView).getChildAt(0) as LinearLayout).addView(
                    newImage
                )
                context?.let { context ->
                    Glide.with(context)
                        .load(GlideUrl(it, getAuth()))
                        .apply(
                            RequestOptions
                                .centerCropTransform()
                                .override(80.dp(context))
                        )
                        .into(newImage)
                }
                stepFileKeys[stepIndex].add(urlParts[urlParts.size - 1])
                newImage.setOnLongClickListener {
                    val index =
                        ((ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView)).indexOfChild(
                            it
                        )
                    stepFileKeys[stepIndex].removeAt(index)
                    (ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView).removeView(it)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun showImage(images: List<RecipeImagesViewModel>) {
        for (image in images) {
            val urlParts = image.url.split("/").toTypedArray()
            imagesToRemove.add(urlParts[urlParts.size - 1])
            if (urlParts[urlParts.lastIndex] != "258b7a0b-8441-4c32-88a8-3194771ba86a.png") {
                val photo = ImageView(context)
                ui.imagesList.addView(photo)
                context?.let {
                    Glide.with(it)
                        .load(GlideUrl(image.url, getAuth()))
                        .apply(
                            RequestOptions
                                .centerCropTransform()
                                .override(80.dp(it))
                        )
                        .into(photo)
                }

                fileKeys.add(urlParts[urlParts.size - 1])
                photo.setOnLongClickListener {
                    fileKeys.removeAt(ui.imagesList.indexOfChild(it))
                    ui.imagesList.removeView(it)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun addPhoto() {
        Toast.makeText(requireContext(), "Фото добавлено", Toast.LENGTH_LONG).show()
    }

    override fun showStepImage(images: List<RecipeImagesViewModel>) {
        for (image in images) {
            val photo = ImageView(context)
            ((ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView).getChildAt(0) as LinearLayout).addView(
                photo
            )
            context?.let {
                Glide.with(it)
                    .load(GlideUrl(image.url, getAuth()))
                    .apply(
                        RequestOptions
                            .centerCropTransform()
                            .override(80.dp(it))
                    )
                    .into(photo)
            }
            val urlParts = image.url.split("/").toTypedArray()
            stepFileKeys[stepIndex].add(urlParts[urlParts.size - 1])
            photo.setOnLongClickListener {
                val index =
                    ((ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView).getChildAt(0) as LinearLayout).indexOfChild(
                        it
                    )
                stepFileKeys[stepIndex].removeAt(index)
                (ui.steps.getChildAt(stepIndex * 5 + 3) as HorizontalScrollView).removeView(it)
                return@setOnLongClickListener true
            }
        }
    }

    override fun loadStepImages(stepImages: Map<String, List<RecipeImages>>) {
        for (i in 0 until stepImages.size) {
            val stepUrls = stepImages[i.toString()]
            stepUrls?.let {
                for (image in it) {
                    val urlParts = image.url.split("/").toTypedArray()
                    stepImagesToRemove[i].add(urlParts[urlParts.size - 1])
                    if (urlParts[urlParts.lastIndex] != "258b7a0b-8441-4c32-88a8-3194771ba86a.png") {
                        stepFileKeys[i].add(urlParts[urlParts.size - 1])
                        val exPhoto = ImageView(context)
                        (((ui.steps.getChildAt(i * 5 + 3) as HorizontalScrollView).getChildAt(0)) as LinearLayout).addView(
                            exPhoto
                        )
                        context?.let { context ->
                            Glide.with(context)
                                .load(GlideUrl(image.url, getAuth()))
                                .apply(
                                    RequestOptions
                                        .centerCropTransform()
                                        .override(80.dp(context))
                                )
                                .into(exPhoto)
                        }
                        exPhoto.setOnLongClickListener { photo ->
                            stepFileKeys[i].removeAt(
                                ((ui.steps.getChildAt(i * 5 + 3) as HorizontalScrollView).getChildAt(
                                    0
                                ) as LinearLayout).indexOfChild(photo)
                            )
                            ((ui.steps.getChildAt(i * 5 + 3) as HorizontalScrollView).getChildAt(0) as LinearLayout).removeView(
                                photo
                            )
                            return@setOnLongClickListener true
                        }
                    }
                }
            }
        }
    }

    override fun loadTags(tags: List<TagViewModel>) {
        val tagsAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tagsNameList
            )
        tagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for (tag in tags) {
            val newTagline = LinearLayout(context)
            newTagline.orientation = LinearLayout.VERTICAL
            val tagName = Spinner(context)
            tagName.adapter = tagsAdapter
            newTagline.addView(tagName)
            for (t in tagsNameList) {
                if (t == tag.briefName) {
                    tagName.setSelection(tagsNameList.indexOf(t))
                    break
                }
            }
            val removeButton = Button(context)
            removeButton.text = "Удалить тэг"
            removeButton.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            removeButton.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.pink,
                    requireActivity().theme
                )
            )
            removeButton.setOnClickListener {
                removeTag(newTagline)
            }
            newTagline.addView(removeButton)
            ui.tagLayout.addView(newTagline)
            removeButton.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            removeButton.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
    }

    override fun photoUploading() {
        photoCount--
        if (photoCount <= 0) {
            val navController = findNavController()
            val bundle = Bundle()
            bundle.putString("RecipeID", recipeId)
            navController.navigate(R.id.recipeDetailsFragment, bundle)
        }
    }

    override fun success() {
        println("success")
    }

    private fun addTag() {
        val tagsAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tagsNameList
            )
        tagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val newTagline = LinearLayout(context)
        newTagline.orientation = LinearLayout.HORIZONTAL
        val tagName = Spinner(context)
        tagName.adapter = tagsAdapter
        tagName.tooltipText = "Выберите тэг"
        val removeButton = Button(context)
        removeButton.setTextColor(resources.getColor(R.color.white, requireActivity().theme))
        removeButton.text = "Удалить тэг"
        removeButton.backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.pink,
                requireActivity().theme
            )
        )
        removeButton.setOnClickListener {
            removeTag(newTagline)
        }
        newTagline.addView(tagName)
        newTagline.addView(removeButton)
        ui.tagLayout.addView(newTagline)
        removeButton.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        removeButton.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
    }


    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val navController = findNavController()
                navController.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun checkFileKey(imageName: String): Boolean {
        var isUnique = true
        for (file in fileKeys) {
            if (imageName == file) {
                isUnique = false
            }
        }
        return isUnique
    }

}
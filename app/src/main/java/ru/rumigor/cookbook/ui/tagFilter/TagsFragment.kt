package ru.rumigor.cookbook.ui.tagFilter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.databinding.ViewSearchbytagsBinding
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.TagViewModel
import ru.rumigor.cookbook.ui.abs.AbsFragment
import ru.rumigor.cookbook.ui.tagFilter.adapter.TagsAdapter
import javax.inject.Inject

class TagsFragment: AbsFragment(R.layout.view_searchbytags), TagsView, TagsAdapter.Delegate {

    @Inject
    lateinit var schedulers: Schedulers
    @Inject
    lateinit var recipeRepository: RecipeRepository

    private val presenter: TagsPresenter by moxyPresenter {
        TagsPresenter(
            recipeRepository,
            schedulers
        )
    }

    private val viewBinding: ViewSearchbytagsBinding by viewBinding()
    private val tagsAdapter = TagsAdapter(delegate = this)
    private var stringBuffer = StringBuffer()

    private var tagList = mutableListOf<TagViewModel>()

    override fun loadTags(tags: List<TagViewModel>) {
        tagList.addAll(tags)
        tagsAdapter.submitList(tagList)
    }


    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        Log.d("ERROR", error.message.toString())
    }

    override fun onTagSelected(tagName: String) {
        stringBuffer.append(",$tagName")
        for (tag in tagList){
            if (tagName == tag.briefName){
                tag.isChecked = true
                break
            }
        }
    }

    override fun onTagDeselected(tagName: String) {
        val text: String = stringBuffer.toString().replace(",$tagName", "")
        stringBuffer = StringBuffer(text)
        println(stringBuffer)
        for (tag in tagList) {
            if (tagName == tag.briefName) {
                tag.isChecked = false
                break
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.ingredientsList.adapter = tagsAdapter
        viewBinding.searchButton.setOnClickListener {
            if (stringBuffer.isNotEmpty()) {
                stringBuffer.deleteCharAt(0)
                val bundle = Bundle()
                val navController = findNavController()
                bundle.putString("TAG_FILTER", stringBuffer.toString())
                navController.navigate(R.id.recipesListFragment, bundle)
                stringBuffer = StringBuffer("")
                for(tag in tagList){
                    tag.isChecked = false
                }
                tagsAdapter.submitList(tagList)
            }
        }
        viewBinding.filter.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               query?.let {tagName ->
                   val filteredTags = mutableListOf<TagViewModel>()
                   for (tag in tagList) {
                       if (tag.briefName.contains(tagName)){
                           filteredTags.add(tag)
                       }
                   }
                   tagsAdapter.submitList(filteredTags)
               }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        }
        )
        viewBinding.filter.setOnCloseListener{
            tagsAdapter.submitList(tagList)
            return@setOnCloseListener true
        }
    }

}
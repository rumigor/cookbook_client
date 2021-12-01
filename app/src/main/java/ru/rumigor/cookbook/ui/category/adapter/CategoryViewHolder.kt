package ru.rumigor.cookbook.ui.category.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rumigor.cookbook.click
import ru.rumigor.cookbook.databinding.CategoryViewBinding
import ru.rumigor.cookbook.ui.CategoryViewModel

class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val viewBinding: CategoryViewBinding by viewBinding()

    fun bind(category: CategoryViewModel, delegate: CategoryAdapter.Delegate?){
        with(viewBinding){
            viewBinding.categoryName.text = category.tittle

            root.click { delegate?.onCategoryPicked(category) }
        }
    }

}
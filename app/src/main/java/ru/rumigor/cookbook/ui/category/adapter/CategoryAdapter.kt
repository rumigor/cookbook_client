package ru.rumigor.cookbook.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.CategoryViewModel

class CategoryAdapter(private val delegate: Delegate): ListAdapter<CategoryViewModel, CategoryViewHolder>(CategoryDiff) {

    interface Delegate{
        fun onCategoryPicked(category: CategoryViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.category_view, parent, false)
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), delegate = delegate)
    }


}
package ru.rumigor.cookbook.ui.category.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.CategoryViewModel

object CategoryDiff: DiffUtil.ItemCallback<CategoryViewModel>() {
    private val payload = Any()
    override fun areItemsTheSame(oldItem: CategoryViewModel, newItem: CategoryViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: CategoryViewModel, newItem: CategoryViewModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: CategoryViewModel, newItem: CategoryViewModel) = payload
}
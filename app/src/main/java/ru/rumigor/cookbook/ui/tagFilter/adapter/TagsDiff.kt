package ru.rumigor.cookbook.ui.tagFilter.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.rumigor.cookbook.ui.TagViewModel


object TagsDiff: DiffUtil.ItemCallback<TagViewModel>() {
    private val payload = Any()

    override fun areItemsTheSame(oldItem: TagViewModel, newItem: TagViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TagViewModel, newItem: TagViewModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: TagViewModel, newItem: TagViewModel) =
        payload
}
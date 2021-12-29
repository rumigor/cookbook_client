package ru.rumigor.cookbook.ui.tagFilter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.TagViewModel

class TagsAdapter (private val delegate: Delegate?):
ListAdapter<TagViewModel, TagsViewHolder>(TagsDiff){


    interface Delegate {
        fun onTagSelected(tagName: String)
        fun onTagDeselected(tagName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder =
        TagsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_tag, parent, false),
            delegate
        )

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {

        holder.bind(getItem(position), delegate)
    }

}
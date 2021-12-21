package ru.rumigor.cookbook.ui.tagFilter.adapter

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rumigor.cookbook.databinding.ViewTagBinding
import ru.rumigor.cookbook.ui.TagViewModel



class TagsViewHolder(view: View, private val delegate: TagsAdapter.Delegate?) : ViewHolder(view) {

    private val viewBiding: ViewTagBinding by viewBinding()

    fun bind(tag: TagViewModel, delegate: TagsAdapter.Delegate?) {
        viewBiding.tagCheckBox.text = tag.briefName
        viewBiding.tagCheckBox.isChecked = tag.isChecked
        viewBiding.tagCheckBox.setOnClickListener {
            if (tag.isChecked){
                (it as CheckBox).isChecked = false
                tag.isChecked = false
                delegate?.onTagDeselected(tagName = tag.briefName)
            } else {
                (it as CheckBox).isChecked = true
                tag.isChecked = true
                delegate?.onTagSelected(tagName = tag.briefName)
            }
        }
    }




}
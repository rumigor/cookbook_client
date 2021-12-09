package ru.rumigor.cookbook.ui.recipeDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.RecipeImagesViewModel



class ImagesAdapter (private val delegate: Delegate): ListAdapter<RecipeImagesViewModel, ImagesViewHolder>(
    ImagesDiff
) {

    interface Delegate {
        fun onImagePicked(image: RecipeImagesViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder =
        ImagesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.dishimageview, parent, false)
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(getItem(position), delegate = delegate)
    }
}
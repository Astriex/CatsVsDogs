package com.astriex.catsvsdogs.ui.fragments.home.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.cats.catsList.AllCatImagesResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class CatsPhotoAdapter :
    PagingDataAdapter<AllCatImagesResponseItem, CatsPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<AllCatImagesResponseItem>() {
            override fun areItemsTheSame(
                oldItem: AllCatImagesResponseItem,
                newItem: AllCatImagesResponseItem
            ): Boolean {
                return oldItem.originalFilename == newItem.originalFilename
            }

            override fun areContentsTheSame(
                oldItem: AllCatImagesResponseItem,
                newItem: AllCatImagesResponseItem
            ): Boolean {
                return oldItem.originalFilename == newItem.originalFilename
            }
        }
    }

    class PhotoViewHolder(private val binding: com.astriex.catsvsdogs.databinding.ItemCatsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: AllCatImagesResponseItem) {
            Glide.with(itemView)
                .load(photo.url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.ivCatPhoto)
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            com.astriex.catsvsdogs.databinding.ItemCatsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

}
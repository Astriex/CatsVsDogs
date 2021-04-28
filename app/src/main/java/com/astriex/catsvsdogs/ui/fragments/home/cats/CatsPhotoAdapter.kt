package com.astriex.catsvsdogs.ui.fragments.home.cats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.cats.catsList.AllCatImagesResponseItem
import com.astriex.catsvsdogs.util.DoubleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

interface CatVoteListener {
    fun saveCatVote()
}

class CatsPhotoAdapter(private val listener: CatVoteListener) :
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
                return oldItem == newItem
            }
        }
    }

    inner class PhotoViewHolder(private val binding: com.astriex.catsvsdogs.databinding.ItemCatsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: AllCatImagesResponseItem) {
            Glide.with(itemView)
                .load(photo.url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.ivCatPhoto)

            itemView.setOnClickListener(object : DoubleClickListener() {
                override fun onDoubleClick(v: View) {
                    listener.saveCatVote()
                }

            })
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
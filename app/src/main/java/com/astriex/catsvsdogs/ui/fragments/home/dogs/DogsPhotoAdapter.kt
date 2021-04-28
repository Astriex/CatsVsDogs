package com.astriex.catsvsdogs.ui.fragments.home.dogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.dogs.dogsList.AllDogImagesResponseItem
import com.astriex.catsvsdogs.databinding.ItemDogsListBinding
import com.astriex.catsvsdogs.util.DoubleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

interface DogVoteListener {
    fun saveDogVote()
}

class DogsPhotoAdapter(private val listener: DogVoteListener) :
    PagingDataAdapter<AllDogImagesResponseItem, DogsPhotoAdapter.PhotoViewHolder>(
        COMPARATOR_OBJECT
    ) {

    companion object {
        private val COMPARATOR_OBJECT = object : DiffUtil.ItemCallback<AllDogImagesResponseItem>() {
            override fun areItemsTheSame(
                oldItem: AllDogImagesResponseItem,
                newItem: AllDogImagesResponseItem
            ): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(
                oldItem: AllDogImagesResponseItem,
                newItem: AllDogImagesResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class PhotoViewHolder(private val binding: ItemDogsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: AllDogImagesResponseItem) {
            Glide.with(itemView)
                .load(photo.url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.ivDogPhoto)

            itemView.setOnClickListener(object : DoubleClickListener() {
                override fun onDoubleClick(v: View) {
                    listener.saveDogVote()
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
            ItemDogsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

}
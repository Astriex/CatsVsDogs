package com.astriex.catsvsdogs.ui.fragments.home.cats

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.unsplashList.UnsplashPhoto
import com.astriex.catsvsdogs.util.DoubleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


interface CatVoteListener {
    fun saveCatVote()
}

class CatsPhotoAdapter(private val listener: CatVoteListener, private val context: Context) :
    PagingDataAdapter<UnsplashPhoto, CatsPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PhotoViewHolder(private val binding: com.astriex.catsvsdogs.databinding.ItemCatsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(object : DoubleClickListener() {
                @SuppressLint("RestrictedApi")
                override fun onDoubleClick(v: View) {
                    listener.saveCatVote()
                    animateLike()
                }
            })
        }

        private fun animateLike() {
            CoroutineScope(Dispatchers.Main).launch {
                binding.ivHeart.isVisible = true
                val likeAnimation = android.view.animation.AnimationUtils.loadAnimation(
                    context,
                    R.anim.pulse_fade_in
                )
                likeAnimation.duration = 1000
                binding.ivHeart.startAnimation(likeAnimation)
                binding.ivHeart.isVisible = false
            }
        }

        fun bind(photo: UnsplashPhoto) {
            Glide.with(itemView)
                .load(photo.urls.small)
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
            com.astriex.catsvsdogs.databinding.ItemCatsListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )

        return PhotoViewHolder(binding)
    }

}
package com.astriex.catsvsdogs.ui.fragments.home.dogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashPhoto
import com.astriex.catsvsdogs.databinding.ItemDogsListBinding
import com.astriex.catsvsdogs.util.DoubleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface DogVoteListener {
    fun saveDogVote()
    fun openDetails(photo: UnsplashPhoto)
}

class DogsPhotoAdapter(private val listener: DogVoteListener, private val context: Context) :
    PagingDataAdapter<UnsplashPhoto, DogsPhotoAdapter.PhotoViewHolder>(
        COMPARATOR_OBJECT
    ) {

    companion object {
        private val COMPARATOR_OBJECT = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
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

    inner class PhotoViewHolder(private val binding: ItemDogsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(object: DoubleClickListener() {

                override fun onDoubleClick() {
                    listener.saveDogVote()
                    animateLike()
                }

                override fun onSingleClick() {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        if(item != null) {
                            listener.openDetails(item)
                        }
                    }
                }
            })
        }

        fun bind(photo: UnsplashPhoto) {
            Glide.with(itemView)
                .load(photo.urls.small)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.ivDogPhoto)
            binding.tvUsername.text = photo.user.username
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
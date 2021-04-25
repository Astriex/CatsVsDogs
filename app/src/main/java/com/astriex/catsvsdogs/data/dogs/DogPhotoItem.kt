package com.astriex.catsvsdogs.data.dogs


import com.google.gson.annotations.SerializedName

data class DogPhotoItem(
    val breeds: List<Any>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)
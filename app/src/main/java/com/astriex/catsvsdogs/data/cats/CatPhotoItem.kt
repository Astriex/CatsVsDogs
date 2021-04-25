package com.astriex.catsvsdogs.data.cats


import com.google.gson.annotations.SerializedName

data class CatPhotoItem(
    val breeds: List<Any>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)
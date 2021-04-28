package com.astriex.catsvsdogs.data.networking.dogs.dogsList


import com.google.gson.annotations.SerializedName

data class AllDogImagesResponseItem(
    val breeds: List<Any>,
    val categories: List<Any>,
    val id: String,
    val url: String
)
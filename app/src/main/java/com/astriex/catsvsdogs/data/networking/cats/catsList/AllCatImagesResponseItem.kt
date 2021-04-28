package com.astriex.catsvsdogs.data.networking.cats.catsList

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllCatImagesResponseItem(
    val id: String,
    val url: String,
    @SerializedName("original_filename")
    val originalFilename: String
): Parcelable
package com.astriex.catsvsdogs.data.networking.cats.catsList

import com.astriex.catsvsdogs.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatsListApi {

    companion object {
        const val BASE_URL = "https://api.thecatapi.com/"
        const val API_KEY = BuildConfig.CAT_API_KEY
    }

    @GET("v1/images/search")
    @Headers("X-Api-Key: $API_KEY")
    suspend fun getAllCatImages(
        @Query("size") size: String = "med",
        @Query("limit") limit: Int = 9,
        @Query("page") page: Int = 1,
        @Query("order") order: String = "DESC"
    ): AllCatImagesResponse

}
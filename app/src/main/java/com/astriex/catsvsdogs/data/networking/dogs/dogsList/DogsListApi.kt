package com.astriex.catsvsdogs.data.networking.dogs.dogsList

import com.astriex.catsvsdogs.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DogsListApi {

    companion object {
        const val BASE_URL = "https://api.thedogapi.com/"
        const val API_KEY = BuildConfig.DOG_API_KEY
    }

    @GET("v1/images/search")
    @Headers("x-api-key: $API_KEY")
    suspend fun getAllDogImages(
        @Query("size") size: String = "med",
        @Query("limit") limit: Int = 9,
        @Query("page") page: Int = 1,
        @Query("order") order: String = "DESC"
    ): AllDogImagesResponse

}
package com.astriex.catsvsdogs.data.dogs

import com.astriex.catsvsdogs.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DogsApi {

    companion object {
        const val API_KEY = BuildConfig.DOG_API_KEY
        const val BASE_URL = "https://api.thedogapi.com/"
    }

    @GET("v1/images/search")
    suspend fun getRandomDogPhoto(@Query("api_key") apiKey: String = API_KEY): Response<DogResponse>

}
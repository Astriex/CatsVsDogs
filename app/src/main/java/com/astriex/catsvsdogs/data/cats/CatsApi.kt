package com.astriex.catsvsdogs.data.cats

import com.astriex.catsvsdogs.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    companion object {
        const val BASE_URL = "https://api.thecatapi.com/"
        const val API_KEY = BuildConfig.CAT_API_KEY
    }

    @GET("v1/images/search")
    suspend fun getRandomCatPhoto(@Query("api_key") apiKey: String = API_KEY): Response<CatResponse>

}
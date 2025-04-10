package com.raaveinm.myapplication.restapi

import retrofit2.Response
import retrofit2.http.GET

interface CatApiService {
    @GET("/cat?json=true")
    suspend fun getRandomCat(): Response<CatApiResponse>
}
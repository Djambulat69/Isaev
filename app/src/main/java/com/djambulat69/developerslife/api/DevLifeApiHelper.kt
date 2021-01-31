package com.djambulat69.developerslife.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://developerslife.ru/"

class DevLifeApiHelper {
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService = getRetrofit().create(DevLifeApiService::class.java)

    suspend fun getPosts(category: String, page: Int) = apiService.getPosts(category, page)
}
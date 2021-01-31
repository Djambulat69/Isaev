package com.djambulat69.developerslife.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DevLifeApiService {
    @GET("{category}/{page}?json=true")
    suspend fun getPosts(
        @Path("category") category: String,
        @Path("page") page: Int
    ): DevLifeResponse
}
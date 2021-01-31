package com.djambulat69.developerslife

import com.djambulat69.developerslife.api.DevLifeApiHelper


// object keyword for simple singleton pattern
object Repository {
    private val apiHelper = DevLifeApiHelper()

    suspend fun getPosts(category: String, page: Int) = apiHelper.getPosts(category, page).result
}
package com.djambulat69.developerslife

import com.djambulat69.developerslife.api.DevLifeApiHelper
import com.djambulat69.developerslife.api.DevLifePost


// object keyword for simple singleton pattern
object Repository {
    private val apiHelper = DevLifeApiHelper()

    suspend fun getPosts(category: String, page: Int) = apiHelper.getPosts(category, page).result

    private val cachedPosts = mapOf<String, MutableList<DevLifePost>>(
            "latest" to mutableListOf(),
            "top" to mutableListOf(),
            "hot" to mutableListOf()
    )

    fun getCachedPost(index: Int, category: String): DevLifePost? {
        return cachedPosts[category]?.getOrNull(index)
    }

    fun cachePost(category: String, post: DevLifePost){
        cachedPosts[category]?.add(post)
    }
}
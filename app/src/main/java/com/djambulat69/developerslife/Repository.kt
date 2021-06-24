package com.djambulat69.developerslife

import com.djambulat69.developerslife.api.DevLifeApiHelper
import com.djambulat69.developerslife.api.DevLifePost


object Repository {

    private val apiHelper = DevLifeApiHelper()

    private val cachedPosts = mapOf<String, MutableList<DevLifePost>>(
        "latest" to mutableListOf(),
        "top" to mutableListOf(),
        "hot" to mutableListOf()
    )

    suspend fun getPosts(category: String, page: Int): List<DevLifePost> =
        apiHelper.getPosts(category, page).result

    fun getCachedPost(index: Int, category: String): DevLifePost? {
        return cachedPosts[category]?.getOrNull(index)
    }

    fun cachePost(category: String, post: DevLifePost) {
        cachedPosts[category]?.add(post)
    }
}

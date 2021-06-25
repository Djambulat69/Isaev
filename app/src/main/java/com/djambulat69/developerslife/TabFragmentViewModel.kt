package com.djambulat69.developerslife

import androidx.lifecycle.*
import com.djambulat69.developerslife.api.DevLifePost
import kotlinx.coroutines.Dispatchers

private const val TAG = "TabFragmentViewModel"
private const val PAGE_SIZE = 5

class TabFragmentViewModel(
    private val category: String
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val category: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TabFragmentViewModel(category) as T
        }
    }

    private val mutableCurrentPost: MutableLiveData<Int> = MutableLiveData(0)
    var currentPost = 0
        private set

    var currentPage = 0
        private set

    var post = mutableCurrentPost.switchMap {
        getPostLiveData()
    }
        private set

    private fun getPostLiveData() = liveData<Resource<DevLifePost>>(Dispatchers.IO) {
        emit(Resource.Loading())
        try {

            val mutIndex = mutableCurrentPost.value!!

            val cache = Repository.getCachedPost(currentPost, category)
            if (cache != null) {
                emit(Resource.Success(cache))
            } else {
                val freshFivePosts = Repository.getPosts(category, currentPage)
                freshFivePosts.forEach { Repository.cachePost(category, it) }

                emit(Resource.Success(freshFivePosts[mutIndex]))
            }
        } catch (e: IndexOutOfBoundsException) {
            val resMessage = "No posts"

            emit(Resource.Error(resMessage))
        } catch (e: Exception) {
            emit(Resource.Error(message = "Check internet connection"))
        }
    }

    fun nextPost() {
        currentPost += 1
        mutableCurrentPost.value = currentPost % PAGE_SIZE
        currentPage = currentPost / PAGE_SIZE
    }

    fun prevPost() {
        if (currentPost > 0) {
            currentPost -= 1
            mutableCurrentPost.value = currentPost % PAGE_SIZE
            currentPage = currentPost / PAGE_SIZE
        }
    }

    fun retryPost() {
        post = mutableCurrentPost.switchMap {
            getPostLiveData()
        }
    }

}

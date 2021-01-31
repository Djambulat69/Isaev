package com.djambulat69.developerslife

import android.content.Context
import android.content.res.Resources
import android.content.res.loader.ResourcesLoader
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.lifecycle.*
import com.djambulat69.developerslife.api.DevLifePost
import kotlinx.coroutines.Dispatchers

private const val TAG = "TabFragmentViewModel"

class TabFragmentViewModel(private val category: String,
                           private val context: Context): ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val category: String, private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TabFragmentViewModel(category, context) as T
        }
    }
    private val mutableCurrentPost: MutableLiveData<Int> = MutableLiveData(0)
    var currentPost = 0
        private set

    var currentPage = 0
        private set

    private val cachedPosts = mutableListOf<DevLifePost>()

    var post = mutableCurrentPost.switchMap {
        getPostLiveData()
    }
        private set

    private fun getPostLiveData() = liveData<Resource<DevLifePost>>(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            Log.d(TAG, "currentPage = $currentPost / 5 = $currentPage")

            val mutIndex = mutableCurrentPost.value!!

            val cache = cachedPosts.getOrNull(currentPost)
            if (cache != null) {
                emit(Resource.Success(cache))
            } else {
                val fresh = Repository.getPosts(category, currentPage)[mutIndex]
                cachedPosts.add(fresh)
                emit(Resource.Success(fresh))
            }
        } catch(e: IndexOutOfBoundsException) {
            val resMessage = if (e.message == "Index: 0, Size: 0")
                context.getString(R.string.no_posts_text)
            else
                context.getString(R.string.no_more_posts_text)

            emit(Resource.Error(resMessage))
        } catch (e: Exception){
            Log.d(TAG, "exception: $e")
            emit(Resource.Error(message = context.getString(R.string.connection_error_text)))
        }
    }

    fun nextPost(){
        currentPost += 1
        mutableCurrentPost.value = currentPost % 5
        currentPage = currentPost / 5
    }

    fun prevPost(){
        currentPost -= 1
        mutableCurrentPost.value = currentPost % 5
        currentPage = currentPost / 5
    }

    fun retryPost(){
        post = mutableCurrentPost.switchMap {
            getPostLiveData()
        }
    }

}
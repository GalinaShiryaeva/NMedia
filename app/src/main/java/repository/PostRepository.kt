package repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dto.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}

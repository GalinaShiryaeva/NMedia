package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shared: Int = 0,
    val video: String?
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        shared = shared,
        video = video
    )

    companion object {
        fun fromDto(post: Post): PostEntity =
            with(post) {
                PostEntity(
                    id = id,
                    author = author,
                    content = content,
                    published = published,
                    likedByMe = likedByMe,
                    likes = likes,
                    shared = shared,
                    video = video
                )
            }
    }
}
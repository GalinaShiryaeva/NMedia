package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostDiffCallback
import kotlin.math.floor

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = validateText(post.likes)
            shareCount.text = validateText(post.shared)
            val likeImage = if (post.likedByMe) {
                R.drawable.ic_liked_24
            } else {
                R.drawable.ic_like_24
            }
            like.setImageResource(likeImage)
            like.setOnClickListener {
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
        }
    }
}

fun validateText(number: Int): String {
    return when (number) {
        in 0..999 -> "$number"
        in 1_000..10_000 -> adjustNumberUnder10K(number) + "K"
        in 10_001..999_999 -> adjustNumberUnder1M(number) + "K"
        else -> adjustNumberOver1M(number) + "M"
    }
}

fun adjustNumberUnder10K(number: Int): String {
    val f = floor((number / 1_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}

fun adjustNumberUnder1M(number: Int): String {
    return floor((number / 1_000.0)).toInt().toString()
}

fun adjustNumberOver1M(number: Int): String {
    val f = floor((number / 1_000_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}
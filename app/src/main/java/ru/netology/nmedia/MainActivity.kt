package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import viewmodel.PostViewModel
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                val likeImage = if (post.likedByMe) {
                    R.drawable.ic_liked_24
                } else {
                    R.drawable.ic_like_24
                }
                like.setImageResource(likeImage)
                likeCount.text = validateText(post.likes)
                shareCount.text = validateText(post.shared)
            }
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener {
            viewModel.share()
        }
    }

    fun validateText(number: Int): String {
        return when (number) {
            in 0..999 -> "$number"
            in 1_000..10_000 -> "${floor((number / 1_000.0) * 10.0) / 10.0}K"
            in 10_001..999_999 -> "${floor((number / 1_000.0) * 10.0) / 10.0}K"
            else -> "${floor((number / 1_000_000.0) * 10.0) / 10.0}M"
        }
    }
}

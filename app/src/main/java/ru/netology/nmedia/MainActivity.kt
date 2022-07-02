package ru.netology.nmedia

import Post
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import ru.netology.nmedia.databinding.ActivityMainBinding
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val likes: Int = 999_999
        var shares: Int = 5
        var isLiked = false

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            isLiked = post.likedByMe
            footerLikesNumber.text = validateText(likes)
            footerSharesNumber.text = validateText(shares)
        }

        binding.footerLikesIcon.setOnClickListener {
            isLiked = it.click(likes, binding.footerLikesNumber, isLiked)
            if (isLiked) {
                binding.footerLikesIcon.setImageResource(R.drawable.ic_liked_24)
            } else {
                binding.footerLikesIcon.setImageResource(R.drawable.ic_like_24)
            }
        }

        binding.footerSharesIcon.setOnClickListener {
            binding.footerSharesNumber.text = (++shares).toString()
        }
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

fun View.click(
    defaultValue: Int,
    textView: TextView,
    isClicked: Boolean
): Boolean {
    if (!isClicked) {
        val value = defaultValue + 1
        textView.text = validateText(value)
        return true
    } else {
        textView.text = validateText(defaultValue)
        return false
    }
}

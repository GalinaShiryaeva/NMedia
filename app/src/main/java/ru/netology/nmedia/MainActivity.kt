package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }
            }
        )

        viewModel.edited.observe(this) { edited ->
            if (edited.id == 0L) {
                return@observe
            }
            binding.content.setText(edited.content)
            binding.content.requestFocus()
        }

        binding.save.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(it.context, getString(R.string.empty_post_error), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val text = binding.content.text.toString()



            viewModel.editContent(text)
            viewModel.save()

            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
            binding.content.setText("")
        }

        binding.list.adapter = adapter
        (binding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}

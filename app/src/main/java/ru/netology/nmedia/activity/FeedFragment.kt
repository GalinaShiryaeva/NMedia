package ru.netology.nmedia.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostDetailsFragment.Companion.idArg
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostsAdapter(object : PostEventListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(
                    intent,
                    getString(R.string.chooser_share_post)
                )
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideo(post: Post) {
                val appIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("vnd.youtube:" + post.video)
                )
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + post.video)
                )
                try {
                    startActivity(appIntent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(webIntent)
                }
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postDetailsFragment,
                    Bundle().apply {
                        idArg = post.id
                    }
                )
            }
        })

        binding.list.adapter = adapter
        (binding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.create.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}

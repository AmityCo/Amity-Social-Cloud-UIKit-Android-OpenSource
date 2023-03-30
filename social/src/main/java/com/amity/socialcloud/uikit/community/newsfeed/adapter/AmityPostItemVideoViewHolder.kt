package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmitySpacesItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostVideoClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia


class AmityPostItemVideoViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    private val imageRecyclerView = itemView.findViewById<RecyclerView>(R.id.rvImages)
    private val space = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
    private val itemDecor = AmitySpacesItemDecoration(0, 0, 0, space)
    private var adapter: AmityPostImageChildrenAdapter? = null
    private var videoClickListener = object : AmityPostVideoClickListener {
        override fun onClickVideoThumbnail(
            parentPostId: String,
            images: List<AmityImage>,
            position: Int
        ) {
            postContentClickPublisher.onNext(
                PostContentClickEvent.Video(
                    parentPostId,
                    images,
                    position
                )
            )
        }
    }

    override fun bind(post: AmityPost) {
        setPostText(post, showFullContent)
        val images = mutableListOf<AmityImage>()
        if (post.getChildren().isNotEmpty()) {
            post.getChildren().forEach {
                when (val postData = it.getData()) {
                    is AmityPost.Data.VIDEO -> {
                        postData.getThumbnailImage()?.let { image ->
                            images.add(image)
                        }
                    }
                    else -> {}
                }
            }
            initAdapter(post.getPostId())
            submitImages(images)
        }
    }

    private fun initAdapter(parentPostId: String) {
        if (adapter == null) {
            adapter = AmityPostImageChildrenAdapter(videoClickListener)
            adapter?.setMediaType(PostMedia.Type.VIDEO)
            imageRecyclerView.addItemDecoration(itemDecor)
            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            imageRecyclerView.layoutManager = layoutManager
            imageRecyclerView.adapter = adapter
        }
        adapter?.setParentPostId(parentPostId)
    }

    private fun submitImages(images: List<AmityImage>) {
        adapter?.submitList(images)
    }
}

package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.base.AmitySpacesItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostImageClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostContentItem

class AmityPostItemImageViewHolder(itemView: View): AmityPostContentViewHolder(itemView) {


    private val imageRecyclerView = itemView.findViewById<RecyclerView>(R.id.rvImages)
    private val space = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
    private val itemDecor = AmitySpacesItemDecoration(0, 0, 0, space)
    private var adapter: AmityPostImageChildrenAdapter? = null
    private var imageClickListener = object : AmityPostImageClickListener {
        override fun onClickImage(images: List<AmityImage>, position: Int) {
            postContentClickPublisher.onNext(PostContentClickEvent.Image(images, position))
        }
    }

    override fun bind(post: AmityPost) {
            setPostText(post, showFullContent)
            val images = mutableListOf<AmityImage>()
            if (!post.getChildren().isNullOrEmpty()) {
                post.getChildren().forEach {
                    when (val postData = it.getData()) {
                        is AmityPost.Data.IMAGE -> {
                            postData.getImage()?.let { ekoImage ->
                                images.add(ekoImage)
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
            adapter = AmityPostImageChildrenAdapter(parentPostId, imageClickListener)
            imageRecyclerView.addItemDecoration(itemDecor)
            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            imageRecyclerView.layoutManager = layoutManager
            imageRecyclerView.adapter = adapter
        }
    }

    private fun submitImages(images: List<AmityImage>) {
        adapter?.submitList(images)
    }

}
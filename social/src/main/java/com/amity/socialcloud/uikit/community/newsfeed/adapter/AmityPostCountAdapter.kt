package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.R

class AmityPostCountAdapter : PagingDataAdapter<AmityPost, AmityPostCountViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityPostCountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.amity_item_unknown_post, parent, false)
        return AmityPostCountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmityPostCountViewHolder, position: Int) {

    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<AmityPost>() {
            override fun areItemsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean =
                oldItem.getPostId() == newItem.getPostId()

            override fun areContentsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
                return false
            }
        }
    }
}

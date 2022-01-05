package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import com.amity.socialcloud.sdk.social.feed.AmityPost

class AmityPostItemTextViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    override fun bind(post: AmityPost) {
        setPostText(post, showFullContent)
    }

}
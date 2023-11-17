package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.views.text.AmityExpandableTextView
import com.amity.socialcloud.uikit.common.linkpreview.AmityPreviewLinkView
import com.amity.socialcloud.uikit.community.R

class AmityPostItemTextViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    private val tvPost = itemView.findViewById<AmityExpandableTextView>(R.id.tvFeed)
    private val previewLink = itemView.findViewById<AmityPreviewLinkView>(R.id.viewLinkPreview)

    override fun bind(post: AmityPost) {
        setPostText(post, showFullContent)

        val firstURL = findFirstURL()
        previewLink.loadPreview(post, firstURL)
    }

    private fun findFirstURL(): String? {
        return tvPost.urls.firstOrNull()?.url
    }
}
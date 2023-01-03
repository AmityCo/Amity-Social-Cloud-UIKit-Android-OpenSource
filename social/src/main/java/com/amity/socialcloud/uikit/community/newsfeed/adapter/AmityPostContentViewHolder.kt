package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.common.views.text.AmityExpandableTextView
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.PollVoteClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.subjects.PublishSubject

open class AmityPostContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var postContentClickPublisher = PublishSubject.create<PostContentClickEvent>()
    internal var pollVoteClickPublisher = PublishSubject.create<PollVoteClickEvent>()
    internal var showFullContent = false

    open fun bind(post: AmityPost) {

    }

    internal fun setPostText(data: AmityPost, showCompleteText: Boolean) {
        val tvPost = itemView.findViewById<AmityExpandableTextView>(R.id.tvFeed)
        setPostTextToTextView(tvPost, data, showCompleteText)
    }

    internal fun setPostTextToTextView(tvPost: AmityExpandableTextView, data: AmityPost, showCompleteText: Boolean) {
        tvPost.text = AmitySocialUISettings.postMarkupProcessor.toSpannedText(data)
        tvPost.movementMethod = LinkMovementMethod.getInstance()

        if (showCompleteText) {
            tvPost.showCompleteText()
            tvPost.tag = tvPost.getVisibleLineCount()
        }

        if (tvPost.tag != tvPost.getVisibleLineCount()) {
            tvPost.forceLayout()
            tvPost.tag = tvPost.getVisibleLineCount()
        }

        tvPost.isVisible = tvPost.text.isNotEmpty()

        tvPost.setExpandOnlyOnReadMoreClick(true)
    }
}

package com.amity.socialcloud.uikit.community.newsfeed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemReplyCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import io.reactivex.subjects.PublishSubject

class ReplyCommentViewHolder(
    private val binding: AmityItemReplyCommentBinding,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: AmityComment?, isReadOnly: Boolean) {
        comment?.let {
            binding.viewComment.setComment(comment, null, isReadOnly)
            binding.viewComment.setEventPublishers(userClickPublisher, commentContentClickPublisher, commentEngagementClickPublisher, commentOptionClickPublisher)
        }
    }

}
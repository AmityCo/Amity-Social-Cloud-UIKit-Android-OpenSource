package com.amity.socialcloud.uikit.community.newsfeed.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemReplyCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.ReactionCountClickEvent
import io.reactivex.rxjava3.subjects.PublishSubject

class ReplyCommentViewHolder(
    private val binding: AmityItemReplyCommentBinding,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
    private val reactionCountClickPublisher: PublishSubject<ReactionCountClickEvent>
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: AmityComment?, isReadOnly: Boolean) {
        comment?.let {
            binding.viewComment.setComment(comment, null, isReadOnly)
            binding.viewComment.setEventPublishers(
                userClickPublisher,
                commentContentClickPublisher,
                commentEngagementClickPublisher,
                commentOptionClickPublisher,
                reactionCountClickPublisher
            )
        }
    }

}
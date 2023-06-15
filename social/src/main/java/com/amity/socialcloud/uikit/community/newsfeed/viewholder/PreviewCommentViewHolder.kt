package com.amity.socialcloud.uikit.community.newsfeed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemPreviewCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.ReactionCountClickEvent
import io.reactivex.rxjava3.subjects.PublishSubject

class PreviewCommentViewHolder(
    private val binding: AmityItemPreviewCommentBinding,
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
            if (comment.getChildCount() > 0) {
                binding.viewMoreRepliesContainer.visibility = View.VISIBLE
                binding.viewMoreRepliesContainer.setOnClickListener {
                    commentContentClickPublisher.onNext(
                        CommentContentClickEvent.Text(
                            comment,
                            null
                        )
                    )
                }
            } else {
                binding.viewMoreRepliesContainer.visibility = View.GONE
            }
        }
    }

}
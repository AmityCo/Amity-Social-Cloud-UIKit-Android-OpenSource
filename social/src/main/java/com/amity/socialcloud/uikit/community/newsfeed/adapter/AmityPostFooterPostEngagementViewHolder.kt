package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterPostEngagementBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.ReactionCountClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostFooterPostEngagementViewHolder(
    private val binding: AmityItemPostFooterPostEngagementBinding,
    private val postEngagementClickPublisher: PublishSubject<PostEngagementClickEvent>,
    private val reactionCountClickPublisher: PublishSubject<ReactionCountClickEvent>,
) : AmityPostFooterViewHolder(binding.root) {

    override fun bind(data: AmityBasePostFooterItem, position: Int) {
        binding.executePendingBindings()
        val postEngagementData = data as AmityBasePostFooterItem.POST_ENGAGEMENT
        setNumberOfReactions(postEngagementData.post.getReactionCount())
        val isReactedByMe =
            postEngagementData.post.getMyReactions().contains(AmityConstants.POST_REACTION)
        setUpLikeView(
            isReactedByMe,
            postEngagementData.post.getReactionCount(),
            postEngagementData.post
        )
        setNumberOfComments(postEngagementData.post.getCommentCount())
        setReadOnlyMode(postEngagementData.isReadOnly)
        setShareOption(postEngagementData.post)
        setCommentListener(postEngagementData.post)
        setReactionCountListener(postEngagementData.post)
    }

    private fun setCommentListener(post: AmityPost) {
        binding.cbComment.setOnClickListener {
            postEngagementClickPublisher.onNext(PostEngagementClickEvent.Comment(post))
        }
        binding.tvNumberOfComments.setOnClickListener {
            postEngagementClickPublisher.onNext(PostEngagementClickEvent.Comment(post))
        }
    }

    private fun setReactionCountListener(post: AmityPost) {
        binding.tvNumberOfReactions.setOnClickListener {
            reactionCountClickPublisher.onNext(ReactionCountClickEvent.Post(post))
        }
    }

    private fun setNumberOfComments(commentCount: Int) {
        binding.tvNumberOfComments.visibility = if (commentCount > 0) View.VISIBLE else View.GONE
        binding.tvNumberOfComments.text = binding.root.resources.getQuantityString(
            R.plurals.amity_feed_number_of_comments,
            commentCount,
            commentCount
        )
    }

    private fun setNumberOfReactions(reactionCount: Int) {
        binding.tvNumberOfReactions.visibility = if (reactionCount > 0) View.VISIBLE else View.GONE
        binding.tvNumberOfReactions.text = binding.root.resources.getQuantityString(
            R.plurals.amity_feed_number_of_likes,
            reactionCount,
            reactionCount.readableNumber()
        )
    }

    private fun setUpLikeView(isReactedByMe: Boolean, reactionCount: Int, post: AmityPost) {
        refreshLikeView(isReactedByMe)
        setLikeClickListener(isReactedByMe, reactionCount, post)
    }

    private fun refreshLikeView(isLike: Boolean) {
        binding.cbLike.isChecked = isLike
        setLikeCheckboxText()
    }

    private fun setLikeCheckboxText() {
        if (binding.cbLike.isChecked) {
            binding.cbLike.setText(R.string.amity_liked)
        } else {
            binding.cbLike.setText(R.string.amity_like)
        }
    }

    private fun setLikeClickListener(isReactedByMe: Boolean, reactionCount: Int, post: AmityPost) {
        val convertedValue = !isReactedByMe
        binding.cbLike.setOnClickListener {
            var displayReactionCount = reactionCount + 1
            var reactionEvent = PostEngagementClickEvent.Reaction(post, true)
            if (!convertedValue) {
                displayReactionCount = Math.max(reactionCount - 1, 0)
                reactionEvent = PostEngagementClickEvent.Reaction(post, false)
            }
            postEngagementClickPublisher.onNext(reactionEvent)
            setNumberOfReactions(displayReactionCount)
            setUpLikeView(convertedValue, displayReactionCount, post)
        }
    }

    private fun setReadOnlyMode(isReadOnly: Boolean) {
        binding.readOnly = isReadOnly
    }

    private fun setShareOption(post: AmityPost) {
        val target = post.getTarget()
        binding.cbShare.setOnClickListener {
            postEngagementClickPublisher.onNext(PostEngagementClickEvent.Sharing(post))
        }
        when (target) {
            is AmityPost.Target.USER -> {
                if (target.getUser()?.getUserId() == AmityCoreClient.getUserId()) {
                    if (AmitySocialUISettings.postSharingSettings.myFeedPostSharingTarget.isNotEmpty()) {
                        binding.cbShare.visibility = View.VISIBLE
                    } else {
                        binding.cbShare.visibility = View.GONE
                    }
                } else {
                    if (AmitySocialUISettings.postSharingSettings.userFeedPostSharingTarget.isNotEmpty()) {
                        binding.cbShare.visibility = View.VISIBLE
                    } else {
                        binding.cbShare.visibility = View.GONE
                    }
                }
            }
            is AmityPost.Target.COMMUNITY -> {
                if (target.getCommunity()?.isPublic() != false) {
                    if (AmitySocialUISettings.postSharingSettings.publicCommunityPostSharingTarget.isNotEmpty()) {
                        binding.cbShare.visibility = View.VISIBLE
                    } else {
                        binding.cbShare.visibility = View.GONE
                    }
                } else {
                    if (AmitySocialUISettings.postSharingSettings.privateCommunityPostSharingTarget.isNotEmpty()) {
                        binding.cbShare.visibility = View.VISIBLE
                    } else {
                        binding.cbShare.visibility = View.GONE
                    }
                }
            }
            else -> {}
        }
    }

}
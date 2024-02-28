package com.amity.socialcloud.uikit.community.newsfeed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.utils.AmityConstants

class PostCommentDiffUtil : DiffUtil.ItemCallback<AmityComment>() {

    override fun areItemsTheSame(oldItem: AmityComment, newItem: AmityComment): Boolean {
        return oldItem.getCommentId() == newItem.getCommentId()
    }

    override fun areContentsTheSame(oldItem: AmityComment, newItem: AmityComment): Boolean {
        return oldItem.getCommentId() == newItem.getCommentId()
                && oldItem.isDeleted() == newItem.isDeleted()
                && oldItem.getCreator()?.getDisplayName() == newItem.getCreator()?.getDisplayName()
                && oldItem.getCreator()?.getAvatar()?.getUrl() == newItem.getCreator()?.getAvatar()?.getUrl()
                && oldItem.getReactionCount() == newItem.getReactionCount()
                && (oldItem.getMyReactions().contains(AmityConstants.POST_REACTION) == oldItem.getMyReactions().contains(AmityConstants.POST_REACTION))
                && oldItem.getEditedAt() == newItem.getEditedAt()
                && oldItem.getState() == newItem.getState()
                && areChildrenTheSame(oldItem.getLatestReplies(), newItem.getLatestReplies())
    }

    fun areChildrenTheSame(oldChildren: List<AmityComment>, newChildren: List<AmityComment>) : Boolean {
        if (oldChildren.size != newChildren.size) {
            return false
        } else {
            var isTheSame = true
            for (index in oldChildren.indices) {
                isTheSame = areContentsTheSame(oldChildren.get(index), newChildren.get(index))
                if (!isTheSame) {
                    break
                }
            }
            return isTheSame
        }
    }
}

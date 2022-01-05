package com.amity.socialcloud.uikit.community.newsfeed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.utils.AmityConstants

class PostCommentDiffUtil : DiffUtil.ItemCallback<AmityComment>() {

    override fun areItemsTheSame(oldItem: AmityComment, newItem: AmityComment): Boolean {
        return oldItem.getCommentId() == newItem.getCommentId()
    }

    override fun areContentsTheSame(oldItem: AmityComment, newItem: AmityComment): Boolean {
        return oldItem.getCommentId() == newItem.getCommentId()
                && oldItem.isDeleted() == newItem.isDeleted()
                && oldItem.getUser()?.getDisplayName() == newItem.getUser()?.getDisplayName()
                && oldItem.getUser()?.getAvatar()?.getUrl() == newItem.getUser()?.getAvatar()?.getUrl()
                && oldItem.getReactionCount() == newItem.getReactionCount()
                && (oldItem.getMyReactions().contains(AmityConstants.POST_REACTION) == oldItem.getMyReactions().contains(AmityConstants.POST_REACTION))
                && oldItem.getEditedAt() == newItem.getEditedAt()
                && areChildrenTheSame(oldItem.getLatestReplies(), newItem.getLatestReplies())
    }

    fun areChildrenTheSame(oldChildren: List<AmityComment>, newChildren: List<AmityComment>) : Boolean {
        if(oldChildren.size != newChildren.size) {
            return false
        } else {
            var isTheSame = true
            for(index in oldChildren.indices) {
                isTheSame = areContentsTheSame(oldChildren.get(index), newChildren.get(index))
                if(!isTheSame) {
                    break
                }
            }
            return isTheSame
        }
    }
}

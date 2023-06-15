package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost

sealed class ReactionCountClickEvent(
    val referenceType: AmityReactionReferenceType,
    val referenceId: String
) {

    class Post(val post: AmityPost) :
        ReactionCountClickEvent(AmityReactionReferenceType.POST, post.getPostId())

    class Comment(val comment: AmityComment) :
        ReactionCountClickEvent(AmityReactionReferenceType.COMMENT, comment.getCommentId())
}
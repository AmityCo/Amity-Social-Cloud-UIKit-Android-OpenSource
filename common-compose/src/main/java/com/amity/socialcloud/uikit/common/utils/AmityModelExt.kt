package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost


fun AmityComment.isCreatorCommunityModerator(): Boolean {
    return (this.getTarget() as? AmityComment.Target.COMMUNITY)
        ?.getCreatorMember()
        ?.getRoles()
        ?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
}

fun AmityPost.isCreatorCommunityModerator(): Boolean {
    return (this.getTarget() as? AmityPost.Target.COMMUNITY)
        ?.getCreatorMember()
        ?.getRoles()
        ?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
}
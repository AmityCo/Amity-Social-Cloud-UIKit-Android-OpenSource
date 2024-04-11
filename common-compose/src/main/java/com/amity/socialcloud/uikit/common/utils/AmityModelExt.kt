package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.utils.AmityConstants


fun AmityComment.isCommunityModerator(): Boolean {
    return (this.getTarget() as? AmityComment.Target.COMMUNITY)
        ?.getCreatorMember()
        ?.getRoles()
        ?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
}
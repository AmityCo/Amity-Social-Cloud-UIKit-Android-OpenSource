package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.AmityVideoResolution
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationModule
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationSettings
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationSettings
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

fun AmityCommunityMember.isModerator(): Boolean {
    return this.getRoles().any {
        it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
    }
}

fun AmityUserNotificationSettings.isSocialNotificationEnabled(): Boolean {
    return this.getModules()?.filterIsInstance<AmityUserNotificationModule.SOCIAL>()
        ?.firstOrNull()?.isEnabled() ?: false
}

fun AmityCommunityNotificationSettings.getPostNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getNotificationEvents().filter {
        it is AmityCommunityNotificationEvent.POST_CREATED ||
                it is AmityCommunityNotificationEvent.POST_REACTED
    }
}

fun AmityCommunityNotificationSettings.getEnabledPostNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getPostNotificationSettings().filter {
        it.isNetworkEnabled()
    }
}

fun AmityCommunityNotificationSettings.isPostNotificationEnabled(): Boolean {
    return this.getPostNotificationSettings().any { it.isNetworkEnabled() }
}

fun AmityCommunityNotificationSettings.getCommentNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getNotificationEvents().filter {
        it is AmityCommunityNotificationEvent.COMMENT_CREATED ||
                it is AmityCommunityNotificationEvent.COMMENT_REACTED ||
                it is AmityCommunityNotificationEvent.COMMENT_REPLIED
    }
}

fun AmityCommunityNotificationSettings.getEnabledCommentNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getCommentNotificationSettings().filter {
        it.isNetworkEnabled()
    }
}

fun AmityCommunityNotificationSettings.isCommentNotificationEnabled(): Boolean {
    return this.getCommentNotificationSettings().any { it.isNetworkEnabled() }
}

fun AmityCommunityNotificationSettings.getStoryNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getNotificationEvents().filter {
        it is AmityCommunityNotificationEvent.STORY_CREATED ||
                it is AmityCommunityNotificationEvent.STORY_COMMENT_CREATED ||
                it is AmityCommunityNotificationEvent.STORY_REACTED
    }
}

fun AmityCommunityNotificationSettings.getEnabledStoryNotificationSettings(): List<AmityCommunityNotificationEvent> {
    return this.getStoryNotificationSettings().filter {
        it.isNetworkEnabled()
    }
}

fun AmityCommunityNotificationSettings.isStoryNotificationEnabled(): Boolean {
    return this.getStoryNotificationSettings().any { it.isNetworkEnabled() }
}

fun AmityVideo.getVideoUrlWithFallbackQuality(): String? {
    val resolutions = this.getResolutions()

    return if (resolutions.contains(AmityVideoResolution.RES_1080)) {
        this.getVideoUrl(AmityVideoResolution.RES_1080)
    } else if (resolutions.contains(AmityVideoResolution.RES_720)) {
        this.getVideoUrl(AmityVideoResolution.RES_720)
    } else if (resolutions.contains(AmityVideoResolution.RES_480)) {
        this.getVideoUrl(AmityVideoResolution.RES_480)
    } else if (resolutions.contains(AmityVideoResolution.RES_360)) {
        this.getVideoUrl(AmityVideoResolution.RES_360)
    } else {
        this.getVideoUrl(AmityVideoResolution.ORIGINAL)
    }
}
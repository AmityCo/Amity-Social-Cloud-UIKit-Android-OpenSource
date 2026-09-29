package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.uikit.common.config.AmityUIKitDataGate
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
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
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE || it == AmityConstants.CHANNEL_MODERATOR_ROLE
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

/**
 * Whether the network has the SOCIAL notification module switched on.
 *
 * Android's stand-in for what iOS reads as
 * AmityCommunityNotificationSettings.isSocialNetworkEnabled. The Android SDK does not surface that
 * flag on the settings object, but it carries the same information per event: the backend sets
 * isNetworkEnabled from the network-level module switch.
 *
 * Folded over the three social categories rather than every event, because the event list also
 * carries LIVESTREAM_START ("video-streaming.didStart"), which belongs to the VIDEO_STREAMING
 * module, not SOCIAL. Counting it would report the social module enabled on a network that has
 * only video streaming on, and would show a Notifications row whose page has nothing to configure.
 */
fun AmityCommunityNotificationSettings.isSocialNetworkEnabled(): Boolean {
    return this.isPostNotificationEnabled() ||
            this.isCommentNotificationEnabled() ||
            this.isStoryNotificationEnabled()
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
    // Either source can withhold it: the network switches the events off, and so
    // does the customer switching the module off. Read once here — both screens
    // that decide whether to offer this row read through this function, and a
    // row offered for a module the app never renders is a setting for nothing.
    if (!AmityUIKitDataGate.isOn(AmityUIKitFeature.POST)) return false
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
    // Either source can withhold it: the network switches the events off, and so
    // does the customer switching the module off. Read once here — both screens
    // that decide whether to offer this row read through this function, and a
    // row offered for a module the app never renders is a setting for nothing.
    if (!AmityUIKitDataGate.isOn(AmityUIKitFeature.COMMENT)) return false
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
    // Either source can withhold it: the network switches the events off, and so
    // does the customer switching the module off. Read once here — both screens
    // that decide whether to offer this row read through this function, and a
    // row offered for a module the app never renders is a setting for nothing.
    if (!AmityUIKitDataGate.isOn(AmityUIKitFeature.STORY)) return false
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
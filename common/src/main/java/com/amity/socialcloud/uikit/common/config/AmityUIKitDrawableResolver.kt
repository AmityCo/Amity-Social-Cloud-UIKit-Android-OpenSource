package com.amity.socialcloud.uikit.common.config

import androidx.annotation.DrawableRes
import com.amity.socialcloud.uikit.common.R

object AmityUIKitDrawableResolver {

    private val MAPPER = listOf(
        "close.png" to R.drawable.amity_ic_close,
        "lock.png" to R.drawable.amity_ic_lock1,
        "threeDot.png" to R.drawable.amity_ic_more_horiz,
        "back.png" to R.drawable.amity_ic_arrow_back,
        "aspect_ratio.png" to R.drawable.amity_ic_aspect_ratio,
        "share_story_button.png" to R.drawable.amity_ic_arrow_forward,
        "hyperlink_button.png" to R.drawable.amity_ic_hyperlink,
        "message_reaction_heart.png" to R.drawable.amity_ic_reaction_heart,
        "message_reaction_like.png" to R.drawable.amity_ic_reaction_like,
        "message_reaction_fire.png" to R.drawable.amity_ic_reaction_fire,
        "message_reaction_grinning.png" to R.drawable.amity_ic_reaction_grinning,
        "message_reaction_sad.png" to R.drawable.amity_ic_reaction_sad,
        "searchButtonIcon" to R.drawable.amity_ic_global_search,
        "search" to R.drawable.amity_ic_search,
        "clear" to R.drawable.amity_ic_close,
        "postCreationIcon" to R.drawable.amity_ic_post_creation,
        "emptyFeedIcon" to R.drawable.amity_ic_empty_newsfeed,
        "exploreCommunityIcon" to R.drawable.amity_ic_social_globe,
        "lockIcon" to R.drawable.amity_ic_lock1,
        "officialBadgeIcon" to R.drawable.amity_ic_verified,
        "backButtonIcon" to R.drawable.amity_ic_social_back,
        "menuIcon" to R.drawable.amity_ic_more_horiz,
        "badgeIcon" to R.drawable.amity_ic_moderator_social,
        "commentButtonIcon" to R.drawable.amity_ic_post_comment,
        "closeIcon" to R.drawable.amity_ic_close,
        "postAttachmentCamera" to R.drawable.amity_ic_post_attachment_camera,
        "postAttachmentPhoto" to R.drawable.amity_ic_post_attachment_photo,
        "postAttachmentVideo" to R.drawable.amity_ic_post_attachment_video,
        "postAttachmentFile" to R.drawable.amity_ic_post_attachment_file,
        "postCreate" to R.drawable.amity_ic_post_create,
        "storyCreate" to R.drawable.amity_ic_create_story_social,
        "verifiedBadge" to R.drawable.amity_ic_verified,
        "plusIcon" to R.drawable.amity_ic_plus_button,
        "communityFeedIcon" to R.drawable.amity_ic_community_feed,
        "communityPinIcon" to R.drawable.amity_ic_community_pin,
        "backIcon" to R.drawable.amity_ic_social_back,
        "threeDotIcon" to R.drawable.amity_ic_more_horiz,
        "communityAnnouncementBadge" to R.drawable.amity_ic_announcement_badge,
        "communityPinBadge" to R.drawable.amity_ic_pin_badge,
    )

    @DrawableRes
    fun getDrawableRes(name: String): Int {
        return MAPPER.firstOrNull { it.first == name }?.second ?: R.drawable.amity_ic_warning
    }
}
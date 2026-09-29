package com.amity.socialcloud.uikit.community.compose.notificationtray

import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature

/**
 * The tray is one surface fed by many modules, so no single module owns it —
 * owning it would empty the whole tray, items from modules the customer still
 * has included, when that one module went (features.json `_unowned_pages`).
 * Each item is gated instead, on tap: it asks about its destination page and
 * its subject, and the tray says so instead of opening a page that is gone.
 */
internal fun AmityNotificationTrayItem.subjectModule(): AmityUIKitFeature? =
    when (getActionType()) {
        "post" -> AmityUIKitFeature.POST
        "poll" -> AmityUIKitFeature.POLL
        "comment", "reply" -> AmityUIKitFeature.COMMENT
        "reaction" -> AmityUIKitFeature.REACTION
        "follow" -> AmityUIKitFeature.USER_RELATIONSHIP
        "event" -> AmityUIKitFeature.EVENTS
        "join_request" -> AmityUIKitFeature.COMMUNITY
        "invitation" -> if (getTargetType() == "room") AmityUIKitFeature.LIVE else null
        "mention" -> when (getTrayItemCategory()) {
            "mention_in_comment", "mention_in_reply" -> AmityUIKitFeature.COMMENT
            "mention_in_poll" -> AmityUIKitFeature.POLL
            else -> AmityUIKitFeature.POST
        }
        else -> null
    }

/** Whether tapping this item may open [pageId] — null for a destination with no page id. */
internal fun AmityNotificationTrayItem.canOpen(pageId: String?): Boolean {
    val subject = subjectModule()
    if (subject != null && !AmityUIKitConfigController.isFeatureEnabled(subject)) return false
    return pageId == null || !AmityUIKitConfigController.isExcluded("$pageId/*/*")
}

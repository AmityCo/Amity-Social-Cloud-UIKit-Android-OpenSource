package com.amity.socialcloud.uikit.common.localization

import com.amity.socialcloud.uikit.common.compose.R

object AmityCommonStrings {
    fun buildStringKeyMap(): Map<String, Int> = mapOf(
        // Ad system
        "amity_common_ad_sponsored" to R.string.amity_common_ad_sponsored,
        "amity_common_ad_ad_about_title" to R.string.amity_common_ad_ad_about_title,
        "amity_common_ad_ad_why_title" to R.string.amity_common_ad_ad_why_title,
        "amity_common_ad_ad_why_description" to R.string.amity_common_ad_ad_why_description,
        "amity_common_ad_ad_about_advertiser" to R.string.amity_common_ad_ad_about_advertiser,
        "amity_common_ad_ad_advertiser_name" to R.string.amity_common_ad_ad_advertiser_name,

        // Reaction list
        "amity_common_button_no_reactions_yet" to R.string.amity_common_button_no_reactions_yet,
        "amity_common_label_be_first_to_react" to R.string.amity_common_label_be_first_to_react,
        "amity_common_button_reactions_not_available" to R.string.amity_common_button_reactions_not_available,
        "amity_common_button_unable_to_load_reactions" to R.string.amity_common_button_unable_to_load_reactions,
        "amity_common_button_tap_to_remove_reaction" to R.string.amity_common_button_tap_to_remove_reaction,

        // Global behavior
        "amity_common_label_sign_in_to_continue" to R.string.amity_common_label_sign_in_to_continue,
        "amity_common_label_join_community_to_interact" to R.string.amity_common_label_join_community_to_interact,
        "amity_common_label_follow_user_to_interact" to R.string.amity_common_label_follow_user_to_interact,

        // Delete message dialog
        "amity_common_label_message_not_sent" to R.string.amity_common_label_message_not_sent,
        "amity_common_button_delete" to R.string.amity_common_button_delete,
        "amity_common_button_cancel" to R.string.amity_common_button_cancel,

        // Error / empty states
        "amity_common_empty_state_no_comments_yet" to R.string.amity_common_empty_state_no_comments_yet,
        "amity_common_button_select_time" to R.string.amity_common_button_select_time,

        // Preview link
        "amity_common_label_preview_not_available_title" to R.string.amity_common_label_preview_not_available_title,
        "amity_common_label_preview_not_available_message" to R.string.amity_common_label_preview_not_available_message,
        "amity_common_button_delete_story" to R.string.amity_common_button_delete_story,

        // Reaction list tab
        "amity_common_button_all" to R.string.amity_common_button_all,

        // Time strings
        "amity_common_time_time_days_suffix" to R.string.amity_common_time_time_days_suffix,
        "amity_common_time_time_hours_suffix" to R.string.amity_common_time_time_hours_suffix,
        "amity_common_time_time_minutes_suffix" to R.string.amity_common_time_time_minutes_suffix,
        "amity_common_time_time_seconds_suffix" to R.string.amity_common_time_time_seconds_suffix,
        "amity_common_time_just_now" to R.string.amity_common_time_just_now,

        // Reaction display names
        "amity_social_button_reaction_like" to R.string.amity_social_button_reaction_like,
        "amity_social_button_reaction_love" to R.string.amity_social_button_reaction_love,
        "amity_social_button_reaction_fire" to R.string.amity_social_button_reaction_fire,
        "amity_social_button_reaction_happy" to R.string.amity_social_button_reaction_happy,
        "amity_social_button_reaction_sad" to R.string.amity_social_button_reaction_sad,
        "amity_common_button_reaction_like" to R.string.amity_common_button_reaction_like,
        "amity_common_button_reaction_heart" to R.string.amity_common_button_reaction_heart,
        "amity_common_button_reaction_fire" to R.string.amity_common_button_reaction_fire,
        "amity_common_button_reaction_grinning" to R.string.amity_common_button_reaction_grinning,
        "amity_common_button_reaction_sad" to R.string.amity_common_button_reaction_sad,
        "amity_common_empty_state_empty_string" to R.string.amity_common_empty_state_empty_string,
        "amity_common_button_see_more" to R.string.amity_common_button_see_more,

        // iOS key aliases
        "amity_social_button_delete_story" to R.string.amity_social_button_delete_story,

        // Button actions
        "amity_common_button_join" to R.string.amity_common_button_join,
        "amity_common_button_remove" to R.string.amity_common_button_remove,
        "amity_common_button_required_indicator" to R.string.amity_common_button_required_indicator,
        "amity_common_button_see_less" to R.string.amity_common_button_see_less,
        "amity_social_button_replying_to" to R.string.amity_social_button_replying_to,

        // Cross-platform gap keys — common module aliases
        "amity_common_button_reaction_all_tab" to R.string.amity_common_button_reaction_all_tab,

        // Chat compose strings
        "amity_social_toast_message_unreport_failed" to R.string.amity_social_toast_message_unreport_failed,
        "amity_social_modal_dialog_delete_message_title" to R.string.amity_social_modal_dialog_delete_message_title,
        "chat.delete.alert.message" to R.string.amity_chat_delete_alert_message,

        "common.mention.error.title" to R.string.amity_common_mention_error_title,
        "common.mention.error.msg" to R.string.amity_common_mention_error_msg,
        "common.modal.dialog.done.button" to R.string.amity_common_modal_dialog_done_button,

        "common.button.inappropriate.image" to R.string.amity_common_button_inappropriate_image,
        "common.label.choose.different.image" to R.string.amity_common_label_choose_different_image
    )
}

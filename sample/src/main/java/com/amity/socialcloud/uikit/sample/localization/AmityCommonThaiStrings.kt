package com.amity.socialcloud.uikit.sample.localization

/**
 * Thai (th) translations for common-compose strings.
 *
 * Usage in your Application.onCreate():
 *   DefaultAmityCommonStringProvider.setLocale("th", AmityCommonThaiStrings.strings)
 */
object AmityCommonThaiStrings {

    val strings: Map<String, String> = mapOf(

        // Ad system
        "amity_common_ad_sponsored" to "โฆษณา",
        "amity_common_ad_ad_about_title" to "เกี่ยวกับโฆษณานี้",
        "amity_common_ad_ad_why_title" to "ทำไมถึงเห็นโฆษณานี้?",
        "amity_common_ad_ad_why_description" to "คุณเห็นโฆษณานี้เพราะมันถูกแสดงให้ผู้ใช้ทุกคนในระบบ",
        "amity_common_ad_ad_about_advertiser" to "เกี่ยวกับผู้ลงโฆษณา",
        "amity_common_ad_ad_advertiser_name" to "ชื่อผู้ลงโฆษณา: %1\$s",

        // Reaction list
        "amity_common_button_no_reactions_yet" to "ยังไม่มีการแสดงความรู้สึก",
        "amity_common_label_be_first_to_react" to "เป็นคนแรกที่แสดงความรู้สึกต่อ %1\$s นี้!",
        "amity_common_label_unable_to_load_content" to "ไม่สามารถโหลดเนื้อหาได้",
        "amity_common_button_reactions_not_available" to "ไม่สามารถแสดงความรู้สึกต่อ %1\$s นี้ได้",
        "amity_common_button_unable_to_load_reactions" to "ไม่สามารถโหลดการแสดงความรู้สึกได้",
        "amity_common_button_tap_to_remove_reaction" to "แตะเพื่อลบการแสดงความรู้สึก",

        // Global behavior
        "amity_common_label_sign_in_to_continue" to "สร้างบัญชีหรือลงชื่อเข้าใช้เพื่อดำเนินการต่อ",
        "amity_common_label_join_community_to_interact" to "เข้าร่วมชุมชนเพื่อโต้ตอบ",
        "amity_common_label_follow_user_to_interact" to "ติดตามผู้ใช้เพื่อโต้ตอบ",

        // Delete message dialog
        "amity_common_label_message_not_sent" to "ข้อความของคุณไม่ได้ถูกส่ง",
        "amity_common_button_delete" to "ลบ",
        "amity_common_button_cancel" to "ยกเลิก",

        // Error / empty states
        "amity_common_empty_state_no_comments_yet" to "ยังไม่มีความคิดเห็น",
        "amity_common_button_select_time" to "เลือกเวลา",

        // Preview link
        "amity_common_label_preview_not_available_title" to "ไม่มีการแสดงตัวอย่าง",
        "amity_common_label_preview_not_available_message" to "ลิงก์นี้ไม่มีการแสดงตัวอย่าง",

        // Reaction list tab
        "amity_common_button_all" to "ทั้งหมด",

        // Time strings
        "amity_common_time_time_days_suffix" to "ว",
        "amity_common_time_time_hours_suffix" to "ชม",
        "amity_common_time_time_minutes_suffix" to "น",
        "amity_common_time_time_seconds_suffix" to "วิ",
        "amity_common_time_just_now" to "เมื่อกี้",
        "amity_common_time_today" to "วันนี้",
        "amity_common_time_yesterday" to "เมื่อวาน",

        // Reaction display names
        "amity_common_button_reaction_like" to "ถูกใจ",
        "amity_common_button_reaction_love" to "รัก",
        "amity_common_button_reaction_fire" to "ไฟ",
        "amity_common_button_reaction_happy" to "มีความสุข",
        "amity_common_button_reaction_sad" to "เศร้า",
        "amity_common_button_reaction_heart" to "หัวใจ",
        "amity_common_button_reaction_grinning" to "ยิ้มกว้าง",

        // Story
        "amity_common_button_delete_story" to "ลบสตอรี่",
        "amity_social_button_delete_story" to "ลบสตอรี่",

        // Search
        "amity_common_placeholder_search_category_hint" to "ค้นหาหมวดหมู่",

        // General actions
        "amity_common_button_see_more" to "ดูเพิ่มเติม",
        "amity_common_button_join" to "เข้าร่วม",
        "amity_common_button_reaction_all_tab" to "ทั้งหมด",
        "amity_common_button_remove" to "ลบออก",
        "amity_common_button_required_indicator" to " *",
        "amity_common_button_see_less" to "ดูน้อยลง",
        "amity_common_empty_state_empty_string" to "",
        "amity_social_button_replying_to" to "ตอบกลับ",

        // Reactions
        "amity_social_button_reaction_like" to "ถูกใจ",
        "amity_social_button_reaction_love" to "รัก",
        "amity_social_button_reaction_fire" to "เจ๋ง",
        "amity_social_button_reaction_happy" to "ฮา",
        "amity_social_button_reaction_sad" to "เศร้า",
    )
}

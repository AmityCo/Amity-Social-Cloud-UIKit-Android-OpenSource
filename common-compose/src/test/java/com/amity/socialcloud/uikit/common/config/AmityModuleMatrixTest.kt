package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.grantAllExcept
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * The whole Phase 1 matrix: for every module, withholding it must exclude every
 * surface it owns, and must not exclude a surface owned by a module that is
 * still available.
 *
 * The lists are the ids Android actually declares, crossed with the owner maps
 * `apollo features sync` generated from apollo/features.json (2026-09-25).
 * Listing every id in the shared table instead would assert against surfaces
 * Android does not render — green proving the table, not the app.
 */
class AmityModuleMatrixTest {

    private val surfaces: Map<String, List<String>> = mapOf(
        "community" to listOf(
            "all_categories_page/*/*",
            "communities_by_category_page/*/*",
            "community_add_category_page/*/*",
            "community_add_member_page/*/*",
            "community_comments_notification_page/*/*",
            "community_invite_member_page/*/*",
            "community_membership_page/*/*",
            "community_notification_page/*/*",
            "community_post_permission_page/*/*",
            "community_posts_notification_page/*/*",
            "community_profile_page/*/*",
            "community_setting_page/*/*",
            "community_setup_page/*/*",
            "community_stories_notification_page/*/*",
            "community_story_setting_page/*/*",
            "pending_posts_page/*/*",
            "pending_request_page/*/*",
            "*/community_categories/*",
            "*/community_header/*",
            "*/join_request_content/*",
            "*/my_communities/*",
            "*/pending_post_content/*",
            "*/pending_post_list/*",
            "*/*/category",
            "*/*/category_avatar",
            "*/*/category_display_name",
            "*/*/close_community",
            "*/*/close_community_description",
            "*/*/communities_button",
            "*/*/community_about_title",
            "*/*/community_add_member_button",
            "*/*/community_add_member_title",
            "*/*/community_avatar",
            "*/*/community_category_name",
            "*/*/community_category_title",
            "*/*/community_cover",
            "*/*/community_display_name",
            "*/*/community_info",
            "*/*/community_invite_member_description",
            "*/*/community_invite_member_title",
            "*/*/community_members_count",
            "*/*/community_membership_description",
            "*/*/community_membership_sub_description",
            "*/*/community_membership_title",
            "*/*/community_name",
            "*/*/community_name_title",
            "*/*/community_official_badge",
            "*/*/community_pending_post",
            "*/*/community_privacy_private_and_hidden_description",
            "*/*/community_privacy_private_and_hidden_icon",
            "*/*/community_privacy_private_and_hidden_title",
            "*/*/community_privacy_private_and_visible_description",
            "*/*/community_privacy_private_and_visible_icon",
            "*/*/community_privacy_private_and_visible_title",
            "*/*/community_privacy_public_description",
            "*/*/community_privacy_public_icon",
            "*/*/community_privacy_public_title",
            "*/*/community_privacy_title",
            "*/*/community_private_badge",
            "*/*/community_profile_actions",
            "*/*/community_title",
            "*/*/community_verify_badge",
            "*/*/create_community_button",
            "*/*/create_community_card",
            "*/*/create_community_icon",
            "*/*/create_community_text",
            "*/*/join_accept_button",
            "*/*/join_decline_button",
            "*/*/leave_community",
            "*/*/members",
            "*/*/moderator_badge",
            "*/*/non_member_section",
            "*/*/pending_invitations"
        ),
        "chat" to listOf(
            "add_group_member_page/*/*",
            "archived_chat_page/*/*",
            "banned_group_member_list_page/*/*",
            "chat_full_text_page/*/*",
            "chat_home_page/*/*",
            "chat_page/*/*",
            "create_conversation_page/*/*",
            "create_group_page/*/*",
            "edit_group_member_permission_page/*/*",
            "edit_group_notification_page/*/*",
            "edit_group_profile_page/*/*",
            "group_chat_page/*/*",
            "group_member_list_page/*/*",
            "group_notification_preference_page/*/*",
            "group_setting_page/*/*",
            "live_chat_page/*/*",
            "message_report_page/*/*",
            "search_channel_page/*/*",
            "select_group_member_page/*/*",
            "*/all_chat_list/*",
            "*/archived_chat_list/*",
            "*/chat_header/*",
            "*/conversation_chat_list/*",
            "*/group_chat_list/*",
            "*/livestream_chat_feed/*",
            "*/message_composer/*",
            "*/message_list/*",
            "*/*/confirm_delete_message",
            "*/*/message_avatar",
            "*/*/message_bubble",
            "*/*/message_bubble_flag",
            "*/*/message_bubble_receiver_text",
            "*/*/message_bubble_sender_text",
            "*/*/message_bubble_timestamp",
            "*/*/message_composer_text_field",
            "*/*/message_option",
            "*/*/message_quick_reaction",
            "*/*/mute_button"
        ),
        "live" to listOf(
            "create_livestream_page/*/*",
            "live_stream_banned_page/*/*",
            "live_stream_declined_page/*/*",
            "livestream_player_page/*/*",
            "livestream_post_target_selection_page/*/*",
            "livestream_terminated_page/*/*",
            "thumbnail_preview_page/*/*",
            "*/create_livestream_bottom_bar/*",
            "*/stream_player/*",
            "*/thumbnail_action/*",
            "*/*/add_thumbnail_button",
            "*/*/cancel_create_livestream_button",
            "*/*/change_thumbnail_button",
            "*/*/close_room_button",
            "*/*/create_livestream_button",
            "*/*/create_livestream_option_button",
            "*/*/create_livestream_settings_button",
            "*/*/delete_thumbnail_button",
            "*/*/end_live_stream_button",
            "*/*/event_host_badge",
            "*/*/live_timer_status",
            "*/*/live_viewer_count_element",
            "*/*/livestream_pinned_product",
            "*/*/livestream_terminated_action_button",
            "*/*/start_livestream_button",
            "*/*/switch_camera_button"
        ),
        "poll" to listOf(
            "poll_post_composer_page/*/*",
            "select_poll_target_page/*/*",
            "*/*/create_poll_button",
            "*/*/poll_add_option_button",
            "*/*/poll_duration_title",
            "*/*/poll_multiple_selection_description",
            "*/*/poll_multiple_selection_title",
            "*/*/poll_options_description",
            "*/*/poll_options_title",
            "*/*/poll_question_title",
            "*/*/post_poll"
        ),
        "userRelationship" to listOf(
            "blocked_users_page/*/*",
            "user_pending_follow_request_page/*/*",
            "user_relationship_page/*/*",
            "*/*/block_user_button",
            "*/*/manage_blocked_users_button",
            "*/*/unblock_user_button",
            "*/*/unfollow_user_button",
            "*/*/user_follower",
            "*/*/user_following"
        ),
        "post" to listOf(
            "clip_feed_page/*/*",
            "create_clip_post_page/*/*",
            "draft_clip_page/*/*",
            "post_composer_page/*/*",
            "post_detail_page/*/*",
            "select_post_target_page/*/*",
            "*/alt_text_config_component/*",
            "*/community_video_feed/*",
            "*/detailed_media_attachment/*",
            "*/hyper_link_config_component/*",
            "*/media_attachment/*",
            "*/post_content/*",
            "*/post_media_preview/*",
            "*/user_feed/*",
            "*/user_image_feed/*",
            "*/user_video_feed/*",
            "*/video_player_page/*",
            "*/*/announcement_badge",
            "*/*/clipsfeed_button",
            "*/*/community_feed_tab_button",
            "*/*/community_media_tab_button",
            "*/*/community_pin_tab_button",
            "*/*/create_clip_button",
            "*/*/create_event_post_button",
            "*/*/create_new_post_button",
            "*/*/create_post_button",
            "*/*/event_discussion_create_post_button",
            "*/*/following_button",
            "*/*/my_timeline_avatar",
            "*/*/my_timeline_text",
            "*/*/pin_badge",
            "*/*/post_accept_button",
            "*/*/post_decline_button",
            "*/*/post_permission",
            "*/*/post_title_label",
            "*/*/user_feed_tab_button",
            "*/*/user_image_feed_tab_button"
        ),
        "story" to listOf(
            "camera_page/*/*",
            "create_story_page/*/*",
            "select_story_target_page/*/*",
            "story_page/*/*",
            "*/story_tab_component/*",
            "*/*/create_story_button",
            "*/*/share_story_button",
            "*/*/story_hyperlink_button",
            "*/*/story_ring",
            "*/*/story_setting"
        ),
        "events" to listOf(
            "event_attendees_page/*/*",
            "event_detail_page/*/*",
            "event_post_target_selection_page/*/*",
            "event_setup_page/*/*",
            "past_events_page/*/*",
            "select_event_target_page/*/*",
            "upcoming_events_page/*/*",
            "*/explore_event_feed_component/*",
            "*/my_event_feed_component/*",
            "*/*/create_event_button",
            "*/*/event_button",
            "*/*/event_details_title",
            "*/*/event_name_title",
            "*/*/event_type_selection",
            "*/*/events_button",
            "*/*/location_bottom_sheet",
            "*/*/timezone_list"
        ),
        "feed" to listOf(
            "*/amity_feed_caught_up_component/*",
            "*/amity_for_you_feed_component/*",
            "*/empty_newsfeed/*",
            "*/global_feed/*",
            "*/newsfeed/*",
            "*/*/empty_feed",
            "*/*/feed_caught_up_cta_button",
            "*/*/feed_caught_up_title",
            "*/*/for_you_button"
        ),
        "comment" to listOf(
            "*/comment_tray_component/*",
            "*/edit_comment_component/*",
            "*/*/comment_button",
            "*/*/story_comment_count"
        ),
        "reaction" to listOf(
            "*/*/livestream_reaction",
            "*/*/message_reaction",
            "*/*/message_reaction_preview",
            "*/*/reaction_button",
            "*/*/reaction_picker",
            "*/*/reaction_preview",
            "*/*/story_reaction_button"
        ),
        "product" to listOf(
            "*/manage_product_tag_list/*",
            "*/product_tag_selection/*",
            "*/*/manage_product_tag",
            "*/*/product_tag_element",
            "*/*/product_tagging_button_element"
        ),
        "ads" to listOf(
            "*/comment_ad/*",
            "*/post_ad/*"
        ),
        "discovery" to listOf(
            "my_communities_search_page/*/*",
            "social_global_search_page/*/*",
            "*/community_search_result/*",
            "*/post_search_result/*",
            "*/recommended_communities/*",
            "*/top_search_bar/*",
            "*/trending_communities/*",
            "*/user_search_result/*",
            "*/*/explore_button",
            "*/*/explore_communities_button",
            "*/*/global_search_button"
        ),
    )

    /** Modules with nothing on this platform that the gate could hide. */
    private val noSurfaceOnAndroid = setOf("pushNotification")

    @Before
    fun setUp() = ModuleGateFixtures.reset()

    @After
    fun tearDown() = ModuleGateFixtures.reset()

    private fun available(key: String) = AmityUIKitConfigController.isFeatureEnabled(key)

    @Test
    fun `every module hides every surface it owns`() {
        for ((module, ids) in surfaces) {
            grantAllExcept(module)
            for (id in ids) {
                assertTrue("$module is withheld but $id is still shown", AmityUIKitConfigController.isExcluded(id))
            }
        }
    }

    @Test
    fun `withholding one module leaves the available ones alone`() {
        for ((module, _) in surfaces) {
            grantAllExcept(module)
            for ((other, ids) in surfaces) {
                if (other == module || !available(other)) continue
                for (id in ids) {
                    assertFalse("$module is withheld and took $other's $id with it", AmityUIKitConfigController.isExcluded(id))
                }
            }
        }
    }

    @Test
    fun `a module with no gateable surface here is declared, not discovered`() {
        // Named so that the day one of them gains a surface, this fails instead
        // of passing quietly.
        assertEquals(
            "a module is in neither set, or in both",
            AmityUIKitFeature.values().map { it.key }.toSet(),
            surfaces.keys + noSurfaceOnAndroid,
        )
        assertTrue(surfaces.keys.intersect(noSurfaceOnAndroid).isEmpty())
    }

    @Test
    fun `with nothing withheld nothing is hidden`() {
        grantAllExcept()
        for ((module, ids) in surfaces) {
            for (id in ids) {
                assertFalse("$module hidden with every module granted", AmityUIKitConfigController.isExcluded(id))
            }
        }
    }
}

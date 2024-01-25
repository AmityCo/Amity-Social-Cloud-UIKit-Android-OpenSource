package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmityStoryMenuCreatorImpl(private val fragment: AmityCommunityBaseNotificationSettingsFragment) :
    AmityStoryMenuCreator {
    override fun createNewStoriesMenu(communityId: String): AmitySettingsItem.TextContent {
        return AmitySettingsItem.TextContent(
            title = R.string.amity_new_stories,
            isTitleBold = true,
            description = R.string.amity_new_stories_description,
            callback = {}
        )
    }

    override fun createNewStoriesRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent {
        return AmitySettingsItem.RadioContent(
            choices = choices,
            callback = fragment::toggleNewStory
        )
    }

    override fun createStoryReactionsMenu(communityId: String): AmitySettingsItem.TextContent {
        return AmitySettingsItem.TextContent(
            title = R.string.amity_story_reactions,
            isTitleBold = true,
            description = R.string.amity_story_reaction_description,
            callback = {}
        )
    }

    override fun createStoryReactionsRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent {
        return AmitySettingsItem.RadioContent(
            choices = choices,
            callback = fragment::toggleReactStory
        )
    }

    override fun createStoryCommentsMenu(communityId: String): AmitySettingsItem.TextContent {
        return AmitySettingsItem.TextContent(
            title = R.string.amity_story_comments,
            isTitleBold = true,
            description = R.string.amity_story_comments_description,
            callback = {}
        )
    }

    override fun createStoryCommentsRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent {
        return AmitySettingsItem.RadioContent(
            choices = choices,
            callback = fragment::toggleStoryComment
        )
    }
}
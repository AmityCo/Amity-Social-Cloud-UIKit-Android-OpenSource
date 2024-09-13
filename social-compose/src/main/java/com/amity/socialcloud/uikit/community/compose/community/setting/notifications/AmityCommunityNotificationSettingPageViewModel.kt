package com.amity.socialcloud.uikit.community.compose.community.setting.notifications

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunityNotificationSettingPageViewModel(override val communityId: String) :
    AmityCommunityNotificationViewModel(communityId) {

    fun updatePostNotificationSettings(
        newPostSetting: AmityCommunityNotificationSettingDataType,
        newPostDefaultSetting: AmityCommunityNotificationSettingDataType,
        reactPostSetting: AmityCommunityNotificationSettingDataType,
        reactPostDefaultSetting: AmityCommunityNotificationSettingDataType,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        val eventModifier = mutableListOf<AmityCommunityNotificationEvent.MODIFIER>()

        if (newPostSetting != newPostDefaultSetting) {
            val data = newPostSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.POST_CREATED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.POST_CREATED.disable()
            }
            eventModifier.add(modifier)
        }

        if (reactPostSetting != reactPostDefaultSetting) {
            val data = reactPostSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.POST_REACTED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.POST_REACTED.disable()
            }
            eventModifier.add(modifier)
        }

        updateNotificationSettings(
            eventModifier = eventModifier,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    fun updateCommentNotificationSettings(
        newCommentSetting: AmityCommunityNotificationSettingDataType,
        newCommentDefaultSetting: AmityCommunityNotificationSettingDataType,
        reactCommentSetting: AmityCommunityNotificationSettingDataType,
        reactCommentDefaultSetting: AmityCommunityNotificationSettingDataType,
        repliedCommentSetting: AmityCommunityNotificationSettingDataType,
        repliedCommentDefaultSetting: AmityCommunityNotificationSettingDataType,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        val eventModifier = mutableListOf<AmityCommunityNotificationEvent.MODIFIER>()

        if (newCommentSetting != newCommentDefaultSetting) {
            val data = newCommentSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.COMMENT_CREATED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.COMMENT_CREATED.disable()
            }
            eventModifier.add(modifier)
        }

        if (reactCommentSetting != reactCommentDefaultSetting) {
            val data = reactCommentSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.COMMENT_REACTED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.COMMENT_REACTED.disable()
            }
            eventModifier.add(modifier)
        }

        if (repliedCommentSetting != repliedCommentDefaultSetting) {
            val data = repliedCommentSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.COMMENT_REPLIED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.COMMENT_REPLIED.disable()
            }
            eventModifier.add(modifier)
        }

        updateNotificationSettings(
            eventModifier = eventModifier,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    fun updateStoryNotificationSettings(
        newStorySetting: AmityCommunityNotificationSettingDataType,
        newStoryDefaultSetting: AmityCommunityNotificationSettingDataType,
        reactStorySetting: AmityCommunityNotificationSettingDataType,
        reactStoryDefaultSetting: AmityCommunityNotificationSettingDataType,
        storyCommentSetting: AmityCommunityNotificationSettingDataType,
        storyCommentDefaultSetting: AmityCommunityNotificationSettingDataType,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        val eventModifier = mutableListOf<AmityCommunityNotificationEvent.MODIFIER>()

        if (newStorySetting != newStoryDefaultSetting) {
            val data = newStorySetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.STORY_CREATED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.STORY_CREATED.disable()
            }
            eventModifier.add(modifier)
        }

        if (reactStorySetting != reactStoryDefaultSetting) {
            val data = reactStorySetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.STORY_REACTED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.STORY_REACTED.disable()
            }
            eventModifier.add(modifier)
        }

        if (storyCommentSetting != storyCommentDefaultSetting) {
            val data = storyCommentSetting.getUpdateModelData()
            val modifier = if (data.first) {
                AmityCommunityNotificationEvent.STORY_COMMENT_CREATED.enable(data.second)
            } else {
                AmityCommunityNotificationEvent.STORY_COMMENT_CREATED.disable()
            }
            eventModifier.add(modifier)
        }

        updateNotificationSettings(
            eventModifier = eventModifier,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    private fun updateNotificationSettings(
        eventModifier: MutableList<AmityCommunityNotificationEvent.MODIFIER>,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        AmityCoreClient.notifications()
            .community(communityId)
            .enable(eventModifier)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

}
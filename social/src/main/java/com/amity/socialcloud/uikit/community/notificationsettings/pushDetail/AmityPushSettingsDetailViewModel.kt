package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import androidx.lifecycle.MutableLiveData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.notification.AmityRolesFilter
import com.amity.socialcloud.sdk.model.core.role.AmityRoles
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationSettings
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.notificationsettings.AmityPushNotificationBaseViewModel
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityPushSettingsDetailViewModel : AmityPushNotificationBaseViewModel() {
    var communityId = ""
    var settingType = ""
    private val map = HashMap<String, AmityCommunityNotificationEvent>()
    val initialStateChanged = MutableLiveData(false)
    var initialReactPost = -1
    var initialNewPost = -1
    var initialReactComment = -1
    var initialNewComment = -1
    var initialReplyComment = -1
    var reactPost = -1
    var newPost = -1
    var reactComment = -1
    var replyComment = -1
    var newComment = -1

    fun setInitialState(id: String?, type: String) {
        communityId = id ?: ""
        settingType = type
    }

    fun getDetailSettingsItem(
        postMenuCreator: AmityPostMenuCreator,
        commentMenuCreator: AmityCommentMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return if (settingType == AmityCommunityPostNotificationSettingsActivity.SettingType.POSTS.name) {
            getPostSettingsItem(postMenuCreator, onResult, onError)
        } else {
            getCommentSettingsItem(commentMenuCreator, onResult, onError)
        }
    }

    private fun getPostSettingsItem(
        menuCreator: AmityPostMenuCreator, onResult: (items: List<AmitySettingsItem>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return getPostSettingsBasedOnPermission(menuCreator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onResult.invoke(it)
            }.doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    private fun getPostSettingsBasedOnPermission(menuCreator: AmityPostMenuCreator): Single<List<AmitySettingsItem>> {
        return getPushNotificationSettings().map { settings ->
            settings.getNotificationEvents().forEach { notificationEvent ->
                when (notificationEvent) {
                    is AmityCommunityNotificationEvent.POST_REACTED -> {
                        map[AmityCommunityNotificationEvent.POST_REACTED.toString()] =
                            notificationEvent
                    }
                    is AmityCommunityNotificationEvent.POST_CREATED -> {
                        map[AmityCommunityNotificationEvent.POST_CREATED.toString()] =
                            notificationEvent
                    }
                    else -> {

                    }
                }
            }
            createPostSettingsItem(menuCreator)
        }
    }

    fun createPostSettingsItem(menuCreator: AmityPostMenuCreator): List<AmitySettingsItem> {
        val settingsItems = mutableListOf<AmitySettingsItem>()
        val separator = AmitySettingsItem.Separator
        val postReactedEvent = map[AmityCommunityNotificationEvent.POST_REACTED.toString()]
        val paddingXS = AmitySettingsItem.Margin(R.dimen.amity_padding_xs)
        if (postReactedEvent != null && postReactedEvent.isNetworkEnabled()) {
            val postReacted = menuCreator.createReactPostMenu(communityId)
            settingsItems.add(postReacted)
            settingsItems.add(paddingXS)

            val reactMenu = menuCreator.createReactPostRadioMenu(
                communityId,
                createPushChoices(postReactedEvent)
            )
            settingsItems.add(reactMenu)
        }
        val newPostEvent = map[AmityCommunityNotificationEvent.POST_CREATED.toString()]
        if (newPostEvent != null && newPostEvent.isNetworkEnabled()) {
            if (initialReactPost != -1) {
                settingsItems.add(separator)
            }
            settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_s))
            val newPost = menuCreator.createNewPostMenu(communityId)
            settingsItems.add(newPost)
            settingsItems.add(paddingXS)

            val newPostMenu =
                menuCreator.createNewPostRadioMenu(communityId, createPushChoices(newPostEvent))
            settingsItems.add(newPostMenu)
        }
        return settingsItems
    }

    private fun createPushChoices(notificationEvent: AmityCommunityNotificationEvent): List<Pair<Int, Boolean>> {
        val choices = ArrayList<Pair<Int, Boolean>>()
        val pair = getInitialValue(notificationEvent)
        val isModerator = pair.second
        when (notificationEvent) {
            is AmityCommunityNotificationEvent.POST_REACTED -> {
                initialReactPost = pair.first
                reactPost = initialReactPost
            }
            is AmityCommunityNotificationEvent.POST_CREATED -> {
                initialNewPost = pair.first
                newPost = initialNewPost
            }
            is AmityCommunityNotificationEvent.COMMENT_CREATED -> {
                initialNewComment = pair.first
                newComment = initialNewComment
            }
            is AmityCommunityNotificationEvent.COMMENT_REPLIED -> {
                initialReplyComment = pair.first
                replyComment = initialReplyComment
            }
            is AmityCommunityNotificationEvent.COMMENT_REACTED -> {
                initialReactComment = pair.first
                reactComment = initialReactComment
            }

            else -> {}
        }
        if (!isGlobalModerator) {
            choices.add(Pair(R.string.amity_everyone, !isModerator))
        }
        choices.add(Pair(R.string.amity_only_moderator, isModerator))
        choices.add(Pair(R.string.amity_notification_off, !notificationEvent.isEnabled()))

        return choices
    }

    private fun getInitialValue(notificationEvent: AmityCommunityNotificationEvent): Pair<Int, Boolean> {
        var isModerator = false
        val initialValue = if (notificationEvent.isEnabled()) {
            val filter = notificationEvent.getRolesFilter()
            isModerator =
                filter is AmityRolesFilter.ONLY && filter.getRoles()
                    .any { it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE } || isGlobalModerator
            if (isModerator) {
                R.string.amity_only_moderator
            } else {
                R.string.amity_everyone
            }
        } else {
            R.string.amity_notification_off
        }
        return Pair(initialValue, isModerator)
    }

    private fun getCommentSettingsItem(
        menuCreator: AmityCommentMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return getCommentsSettingsBasedOnPermission(menuCreator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onResult.invoke(it)
            }.doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    private fun getCommentsSettingsBasedOnPermission(menuCreator: AmityCommentMenuCreator): Single<List<AmitySettingsItem>> {
        return getPushNotificationSettings().map { settings ->

            settings.getNotificationEvents().forEach { notificationEvent ->
                when (notificationEvent) {
                    is AmityCommunityNotificationEvent.COMMENT_REACTED -> {
                        map[AmityCommunityNotificationEvent.COMMENT_REACTED.toString()] =
                            notificationEvent
                    }
                    is AmityCommunityNotificationEvent.COMMENT_CREATED -> {
                        map[AmityCommunityNotificationEvent.COMMENT_CREATED.toString()] =
                            notificationEvent
                    }
                    is AmityCommunityNotificationEvent.COMMENT_REPLIED -> {
                        map[AmityCommunityNotificationEvent.COMMENT_REPLIED.toString()] =
                            notificationEvent
                    }
                    else -> {

                    }
                }
            }
            createCommentSettingsItem(menuCreator)
        }
    }

    fun createCommentSettingsItem(menuCreator: AmityCommentMenuCreator): List<AmitySettingsItem> {
        val settingsItems = mutableListOf<AmitySettingsItem>()
        val separator = AmitySettingsItem.Separator
        val paddingXS = AmitySettingsItem.Margin(R.dimen.amity_padding_xs)
        val paddingS = AmitySettingsItem.Margin(R.dimen.amity_padding_s)
        val reactCommentEvent = map[AmityCommunityNotificationEvent.COMMENT_REACTED.toString()]
        if (reactCommentEvent != null && reactCommentEvent.isNetworkEnabled()) {
            val commentReacted = menuCreator.createReactCommentsMenu(communityId)
            settingsItems.add(commentReacted)
            settingsItems.add(paddingXS)

            val reactMenu =
                menuCreator.createReactCommentsRadioMenu(
                    communityId,
                    createPushChoices(reactCommentEvent)
                )
            settingsItems.add(reactMenu)
        }
        val newCommentEvent = map[AmityCommunityNotificationEvent.COMMENT_CREATED.toString()]
        if (newCommentEvent != null && newCommentEvent.isNetworkEnabled()) {
            if (initialReactComment != -1) {
                settingsItems.add(separator)
                settingsItems.add(paddingS)
            }
            val newComment = menuCreator.createNewCommentsMenu(communityId)
            settingsItems.add(newComment)
            settingsItems.add(paddingXS)

            val newCommentMenu =
                menuCreator.createNewCommentsRadioMenu(
                    communityId,
                    createPushChoices(newCommentEvent)
                )
            settingsItems.add(newCommentMenu)
        }
        val commentReplyEvent = map[AmityCommunityNotificationEvent.COMMENT_REPLIED.toString()]
        if (commentReplyEvent != null && commentReplyEvent.isNetworkEnabled()) {
            if (initialNewComment != -1 || initialReactComment != -1) {
                settingsItems.add(separator)
                settingsItems.add(paddingS)
            }
            val commentReply = menuCreator.createReplyCommentsMenu(communityId)
            settingsItems.add(commentReply)
            settingsItems.add(paddingXS)

            val commentReplyMenu =
                menuCreator.createReplyCommentsRadioMenu(
                    communityId,
                    createPushChoices(commentReplyEvent)
                )
            settingsItems.add(commentReplyMenu)
        }
        return settingsItems
    }

    private fun getPushNotificationSettings(): Single<AmityCommunityNotificationSettings> {
        return AmitySocialClient.newCommunityRepository().notification(communityId)
            .getSettings()
    }

    fun changeState(type: String, value: Int) {
        when (type) {
            AmityCommunityNotificationEvent.POST_REACTED.toString() -> {
                reactPost = value
            }
            AmityCommunityNotificationEvent.POST_CREATED.toString() -> {
                newPost = value
            }
            AmityCommunityNotificationEvent.COMMENT_REACTED.toString() -> {
                reactComment = value
            }
            AmityCommunityNotificationEvent.COMMENT_CREATED.toString() -> {
                newComment = value
            }
            AmityCommunityNotificationEvent.COMMENT_REPLIED.toString() -> {
                replyComment = value
            }
        }
        initialStateChanged.value =
            reactPost != initialReactPost || newPost != initialNewPost || reactComment != initialReactComment ||
                    newComment != initialNewComment || replyComment != initialReplyComment
    }

    fun resetState() {
        reactPost = initialReactPost
        newPost = initialNewPost
        reactComment = initialReactComment
        newComment = initialNewComment
        replyComment = initialReplyComment
    }

    private fun updateInitialState() {
        initialReactPost = reactPost
        initialNewPost = newPost
        initialReactComment = reactComment
        initialNewComment = newComment
        initialReplyComment = replyComment
    }

    fun updatePushNotificationSettings(onComplete: () -> Unit, onError: () -> Unit): Completable {
        initialStateChanged.value = false
        val eventModifier = mutableListOf<AmityCommunityNotificationEvent.MODIFIER>()
        if (settingType == AmityCommunityPostNotificationSettingsActivity.SettingType.POSTS.name) {
            if (reactPost != initialReactPost) {
                val reactPostSetting = getPushSettingUpdateModel(reactPost)
                val modifier = if (reactPostSetting.first) {
                    AmityCommunityNotificationEvent.POST_REACTED.enable(reactPostSetting.second)
                } else {
                    AmityCommunityNotificationEvent.POST_REACTED.disable()
                }
                eventModifier.add(modifier)
            }
            if (newPost != initialNewPost) {
                val newPostSetting = getPushSettingUpdateModel(newPost)
                val modifier = if (newPostSetting.first) {
                    AmityCommunityNotificationEvent.POST_CREATED.enable(newPostSetting.second)
                } else {
                    AmityCommunityNotificationEvent.POST_CREATED.disable()
                }
                eventModifier.add(modifier)
            }
        } else {
            if (reactComment != initialReactComment) {
                val reactCommentSetting = getPushSettingUpdateModel(reactComment)
                val modifier = if (reactCommentSetting.first) {
                    AmityCommunityNotificationEvent.COMMENT_REACTED.enable(reactCommentSetting.second)
                } else {
                    AmityCommunityNotificationEvent.COMMENT_REACTED.disable()
                }
                eventModifier.add(modifier)
            }

            if (newComment != initialNewComment) {
                val newCommentSetting = getPushSettingUpdateModel(newComment)
                val modifier = if (newCommentSetting.first) {
                    AmityCommunityNotificationEvent.COMMENT_CREATED.enable(newCommentSetting.second)
                } else {
                    AmityCommunityNotificationEvent.COMMENT_CREATED.disable()
                }
                eventModifier.add(modifier)
            }

            if (replyComment != initialReplyComment) {
                val replyCommentSetting = getPushSettingUpdateModel(replyComment)
                val modifier = if (replyCommentSetting.first) {
                    AmityCommunityNotificationEvent.COMMENT_REPLIED.enable(replyCommentSetting.second)
                } else {
                    AmityCommunityNotificationEvent.COMMENT_REPLIED.disable()
                }
                eventModifier.add(modifier)
            }
        }
        return AmitySocialClient.newCommunityRepository().notification(communityId)
            .enable(eventModifier)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                updateInitialState()
                onComplete.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    private fun getPushSettingUpdateModel(event: Int): Pair<Boolean, AmityRolesFilter> {
        val isEnable = event != R.string.amity_notification_off
        val rolesFilter = if (event == R.string.amity_only_moderator) {
            AmityRolesFilter.ONLY(
                AmityRoles(
                    listOf(
                        AmityConstants.MODERATOR_ROLE,
                        AmityConstants.COMMUNITY_MODERATOR_ROLE
                    )
                )
            )
        } else {
            AmityRolesFilter.All
        }
        return Pair(isEnable, rolesFilter)
    }

}
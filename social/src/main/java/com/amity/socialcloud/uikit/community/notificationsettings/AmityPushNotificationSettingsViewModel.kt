package com.amity.socialcloud.uikit.community.notificationsettings

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

private const val SAVED_COMMUNITY_ID = "SAVED_NOTIFICATION_SETTINGS_COMMUNITY_ID"

class AmityPushNotificationSettingsViewModel(private val savedState: SavedStateHandle) :
    AmityPushNotificationBaseViewModel() {

    var communityId: String = ""
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    private val isToggleState = PublishSubject.create<Boolean>()

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
    }

    fun getPushNotificationItems(
        menuCreator: AmityPushNotificationMenuCreator,
        startValue: Boolean,
        onResult: (items: List<AmitySettingsItem>) -> Unit
    ): Completable {
        return getItemsBasedOnPermission(menuCreator, startValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onResult)
            .ignoreElements()
    }

    private fun getItemsBasedOnPermission(
        menuCreator: AmityPushNotificationMenuCreator,
        value: Boolean
    ): Flowable<List<AmitySettingsItem>> {
        return getAllNotificationDataSource(value).map { permission ->
            val settingsItems = mutableListOf<AmitySettingsItem>()
            val separator = AmitySettingsItem.Separator
            val allNotifications =
                menuCreator.createAllNotificationsMenu(communityId, Flowable.just(permission))
            settingsItems.add(allNotifications)
            val paddingM1 = AmitySettingsItem.Margin(R.dimen.amity_padding_m1)
            settingsItems.add(paddingM1)
            settingsItems.add(separator)

            if (permission) {
                if (isPostEnabled) {
                    val paddingXS = AmitySettingsItem.Margin(R.dimen.amity_padding_xs)
                    settingsItems.add(paddingXS)
                    val posts = menuCreator.createPostMenu(communityId)
                    settingsItems.add(posts)
                }

                if (isCommentEnabled) {
                    val paddingXXS = AmitySettingsItem.Margin(R.dimen.amity_padding_xxs)
                    settingsItems.add(paddingXXS)
                    val comments = menuCreator.createCommentMenu(communityId)
                    settingsItems.add(comments)
                }

            }
            settingsItems
        }
    }

    private fun getAllNotificationDataSource(value: Boolean): Flowable<Boolean> {
        return getReversionSource().startWith(value)
    }

    private fun getReversionSource(): Flowable<Boolean> {
        return isToggleState.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun revertToggleState(value: Boolean) {
        isToggleState.onNext(value)
    }

    fun updatePushNotificationSettings(enable: Boolean, onError: () -> Unit): Completable {
        val communityNotification =
            AmitySocialClient.newCommunityRepository().notification(communityId)
        val settingsCompletable = if (enable) {
            communityNotification.enable()
        } else {
            communityNotification.disable()
        }
        return settingsCompletable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError.invoke()
            }
    }
}
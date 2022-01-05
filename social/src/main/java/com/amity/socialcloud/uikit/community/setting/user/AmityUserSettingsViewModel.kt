package com.amity.socialcloud.uikit.community.setting.user

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserFollowInfo
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_USER_SETTINGS_USER_ID"

class AmityUserSettingsViewModel (private val savedState: SavedStateHandle) : AmityBaseViewModel() {

    private var userId = ""
    var user: AmityUser? = null
        set(value) {
            userId = value?.getUserId() ?: ""
            savedState.set(SAVED_USER_ID, value?.getUserId())
            field = value
        }

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }

    fun unfollowUser(userId: String): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship().me().unfollow(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun reportUser(user: AmityUser): Completable {
        return user.report().flag()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unReportUser(user: AmityUser): Completable {
        return user.report().unflag()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSettingsItemBasedOnStatus(
        otherUserMenuCreator: AmityOtherUserSettingsMenuCreator,
        selfMenuCreator: AmityUserSettingsMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return if (isSelf()) {
            getSelfSettings(selfMenuCreator)
        }else {
            getOtherUserSettings(otherUserMenuCreator)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onResult)
            .doOnError { onError.invoke() }
            .ignoreElements()

    }

    private fun getOtherUserSettings(
        menuCreator: AmityOtherUserSettingsMenuCreator
    ): Flowable<List<AmitySettingsItem>> {
        return Flowable.combineLatest(
            getUserFollowInfo(),
            getUser(),
            BiFunction { followInfo, user ->
                val settingsItems = mutableListOf<AmitySettingsItem>()
                val separator = AmitySettingsItem.Separator

                settingsItems.add(menuCreator.createManageHeader())
                if (followInfo.getStatus() == AmityFollowStatus.ACCEPTED) {
                    settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                    settingsItems.add(menuCreator.createUnfollowMenu(user.getUserId()))
                }
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(menuCreator.createReportUserMenu(user))
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(separator)
                settingsItems
            }
        )

    }

    private fun getSelfSettings(menuCreator: AmityUserSettingsMenuCreator): Flowable<List<AmitySettingsItem>> {
        val settingsItems = mutableListOf<AmitySettingsItem>()
        val separator = AmitySettingsItem.Separator

        settingsItems.add(menuCreator.createBasicInfoHeader())
        settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
        settingsItems.add(menuCreator.createEditProfileMenu())
        settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
        settingsItems.add(separator)
        return Flowable.just(settingsItems)
    }

    private fun getUser(): Flowable<AmityUser> {
        return AmityCoreClient.newUserRepository()
            .getUser(userId)
    }

    private fun getUserFollowInfo(): Flowable<AmityUserFollowInfo> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .user(userId)
            .getFollowInfo()
    }

    private fun isSelf(): Boolean {
        return AmityCoreClient.getUserId() == userId
    }

}
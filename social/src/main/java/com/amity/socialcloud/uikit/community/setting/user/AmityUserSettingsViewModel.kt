package com.amity.socialcloud.uikit.community.setting.user

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.sdk.model.core.follow.AmityUserFollowInfo
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

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
            .relationship().unfollow(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun reportUser(user: AmityUser): Completable {
        return AmityCoreClient.newUserRepository().flagUser(
            user.getUserId()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unReportUser(user: AmityUser): Completable {
        return AmityCoreClient.newUserRepository().unflagUser(
            user.getUserId()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun blockUser(): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .blockUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unblockUser(): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .unblockUser(userId)
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
                settingsItems.add(menuCreator.createBlockUserMenu(followInfo.getStatus()))
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
package com.amity.socialcloud.uikit.community.setting

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.PermissionViewModel
import com.amity.socialcloud.uikit.community.notificationsettings.AmityPushNotificationBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunitySettingViewModel(private val savedState: SavedStateHandle) :
    AmityPushNotificationBaseViewModel(), PermissionViewModel {

    var communityId: String? = null
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
    }

    fun getCommunity(
        onCommunityLoaded: (AmityCommunity) -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository().getCommunity(communityId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onCommunityLoaded.invoke(it)
            }
            .ignoreElements()

    }

    fun getSettingsItems(
        menuCreator: AmityCommunitySettingsMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return getItemsBasedOnPermissions(menuCreator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onResult)
            .doOnError { onError.invoke() }
            .ignoreElements()
    }

    private fun getItemsBasedOnPermissions(
        menuCreator: AmityCommunitySettingsMenuCreator
    ): Flowable<List<AmitySettingsItem>> {
        val communitySource = AmitySocialClient.newCommunityRepository().getCommunity(communityId!!)
        return Flowable.combineLatest(
            hasCommunityPermission(
                communitySource,
                getCommunityPermissionSource(communityId!!, AmityPermission.EDIT_COMMUNITY)
            ),
            hasCommunityPermission(
                communitySource,
                getCommunityPermissionSource(communityId!!, AmityPermission.DELETE_COMMUNITY)
            ),
            hasCommunityPermission(
                communitySource,
                getCommunityPermissionSource(communityId!!, AmityPermission.REVIEW_COMMUNITY_POST)
            )
        ) { hasEditPermission, hasDeletePermission, hasReviewPermission ->
            val settingsItems = mutableListOf<AmitySettingsItem>()
            val separator = AmitySettingsItem.Separator
            val basicInfo = AmitySettingsItem.Header(title = R.string.amity_basic_info)
            val paddingXXS = AmitySettingsItem.Margin(R.dimen.amity_padding_xxs)

            settingsItems.add(basicInfo)
            if (hasEditPermission) {
                val editProfile = menuCreator.createEditProfileMenu(communityId!!)
                settingsItems.add(editProfile)
                settingsItems.add(paddingXXS)
            }

            val members = menuCreator.createMembersMenu(communityId!!)
            settingsItems.add(members)
            settingsItems.add(paddingXXS)

            if (isGlobalPushEnabled && (isPostEnabled || isCommentEnabled)) {
                val pushStatus = getPushStatus()
                val notification = menuCreator.createNotificationMenu(communityId!!, pushStatus)
                settingsItems.add(notification)
            }

            settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
            settingsItems.add(separator)

            if (hasEditPermission) {
                val communityPermission =
                    AmitySettingsItem.Header(title = R.string.amity_community_permissions)
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(communityPermission)
            }

            if (hasReviewPermission) {
                val postReview = menuCreator.createPostReviewMenu(communityId!!)
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(postReview)
            }

            if (hasReviewPermission) {
                val storySetting = menuCreator.createStorySettingMenu(communityId!!)
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(storySetting)
            }

            if (hasEditPermission || hasDeletePermission) {
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_xs))
                settingsItems.add(separator)
            }

            val leaveCommunity =
                menuCreator.createLeaveCommunityMenu(communityId!!, hasDeletePermission)
            settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_s))
            settingsItems.add(leaveCommunity)
            settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_m1))
            settingsItems.add(separator)

            if (hasDeletePermission) {
                val closeCommunity = menuCreator.createCloseCommunityMenu(communityId!!)
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_s))
                settingsItems.add(closeCommunity)
                settingsItems.add(AmitySettingsItem.Margin(R.dimen.amity_padding_m1))
                settingsItems.add(separator)
            }

            settingsItems
        }
    }

    private fun getPushStatus(): Int {
        return if (isCommunityPushEnabled) {
            R.string.amity_notification_on
        } else {
            R.string.amity_notification_off
        }
    }

    fun leaveCommunity(
        onLeaveSuccess: () -> Unit,
        onLeaveError: (AmityError) -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository()
            .leaveCommunity(communityId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onLeaveSuccess.invoke() }
            .doOnError {
                onLeaveError.invoke(AmityError.from(it))
            }
    }

    fun closeCommunity(
        onCloseSuccess: () -> Unit,
        onCloseError: () -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository().deleteCommunity(communityId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onCloseSuccess.invoke() }
            .doOnError { onCloseError.invoke() }
    }
}

private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"

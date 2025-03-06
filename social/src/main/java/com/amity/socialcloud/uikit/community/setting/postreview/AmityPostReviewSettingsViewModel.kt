package com.amity.socialcloud.uikit.community.setting.postreview

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostReviewSettingsViewModel(private val savedState: SavedStateHandle) :
    AmityBaseViewModel() {

    var communityId: String? = null
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
    }

    private val isToggleState = PublishSubject.create<Boolean>()

    fun getSettingsItems(
        menuCreator: AmityPostReviewSettingsMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit
    ): Completable {
        return getItemsBasedOnPermissions(menuCreator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onResult::invoke)
            .ignoreElement()
    }

    private fun getItemsBasedOnPermissions(
        menuCreator: AmityPostReviewSettingsMenuCreator
    ): Single<List<AmitySettingsItem>> {

        return hasEditCommunityPermission(communityId!!)
            .map { hasEditPermission ->
                val settingsItems = mutableListOf<AmitySettingsItem>()
                val separator = AmitySettingsItem.Separator
                if (hasEditPermission) {
                    val postReviewApprovalMenu = menuCreator.createApproveMemberPostMenu(
                        getNeedPostApprovalDataSource(communityId!!)
                    )
                    settingsItems.add(postReviewApprovalMenu)
                    settingsItems.add(separator)
                }
                settingsItems
            }
    }

    private fun hasEditCommunityPermission(communityId: String): Single<Boolean> {
        return Flowable.combineLatest(
            AmitySocialClient.newCommunityRepository().getCommunity(communityId),
            hasPermissionAtCommunity(AmityPermission.EDIT_COMMUNITY, communityId)
        ) { community, hasEditPermission ->
            if (AmityCoreClient.getUserId() == community.getCreatorId()) {
                true
            } else {
                hasEditPermission
            }
        }.firstOrError()
    }

    fun toggleDecision(
        isChecked: Boolean,
        turnOffEvent: (Boolean) -> Unit,
        turnOnEvent: (Boolean) -> Unit
    ) {
        if (!isChecked) {
            turnOffEvent.invoke(false)
        } else {
            turnOnEvent.invoke(true)
        }
    }

    fun turnOn(onError: () -> Unit): Completable {
        return updateApproveMemberPost(communityId!!, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    fun turnOff(onError: () -> Unit): Completable {
        return updateApproveMemberPost(communityId!!, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    private fun getNeedPostApprovalDataSource(communityId: String): Flowable<Boolean> {
        return Flowable.combineLatest(
            getNeedApprovalState(communityId),
            getReversionSource().startWith(Single.just(false))
        ) { isToggled, _ -> isToggled }
    }

    private fun getNeedApprovalState(communityId: String): Flowable<Boolean> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .map { it.getPostSettings() == AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED }
    }

    private fun getReversionSource(): Flowable<Boolean> {
        return isToggleState.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun revertToggleState() {
        isToggleState.onNext(true)
    }

    private fun updateApproveMemberPost(
        communityId: String,
        isEnable: Boolean
    ): Single<AmityCommunity> {
        val postSettings = if (isEnable) {
            AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED
        } else {
            AmityCommunityPostSettings.ANYONE_CAN_POST
        }
        return AmitySocialClient.newCommunityRepository()
            .editCommunity(communityId)
            .postSettings(postSettings)
            .build()
            .apply()
    }

}


private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"

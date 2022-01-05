package com.amity.socialcloud.uikit.community.setting.postreview

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class AmityPostReviewSettingsViewModel(private val savedState: SavedStateHandle) : AmityBaseViewModel() {

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
    
    private fun hasEditCommunityPermission(communityId: String) : Single<Boolean> {
        return Flowable.combineLatest(
            AmitySocialClient.newCommunityRepository().getCommunity(communityId),
            hasPermissionAtCommunity(AmityPermission.EDIT_COMMUNITY, communityId),
            { community, hasEditPermission ->
                if (AmityCoreClient.getUserId() == community.getUserId()) {
                    true
                } else {
                    hasEditPermission
                }
            }).firstOrError()
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
            getReversionSource().startWith(false), { isToggled, reversionTriggered -> isToggled })
    }

    private fun getNeedApprovalState(communityId: String): Flowable<Boolean> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .map { it.isPostReviewEnabled() }
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
        return AmitySocialClient.newCommunityRepository()
            .updateCommunity(communityId)
            .isPostReviewEnabled(isEnable)
            .build()
            .update()
    }

}


private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"

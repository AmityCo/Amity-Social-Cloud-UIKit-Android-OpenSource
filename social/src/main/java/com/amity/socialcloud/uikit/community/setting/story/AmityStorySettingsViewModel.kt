package com.amity.socialcloud.uikit.community.setting.story


import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityStorySettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityStorySettingsViewModel(private val savedState: SavedStateHandle) : AmityBaseViewModel() {

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
        menuCreator: AmityStorySettingsMenuCreator,
        onResult: (items: List<AmitySettingsItem>) -> Unit
    ): Completable {
        return getItemsBasedOnPermissions(menuCreator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onResult::invoke)
            .ignoreElement()
    }

    private fun getItemsBasedOnPermissions(
        menuCreator: AmityStorySettingsMenuCreator
    ): Single<List<AmitySettingsItem>> {

        return hasEditCommunityPermission(communityId!!)
            .map { hasEditPermission ->
                val settingsItems = mutableListOf<AmitySettingsItem>()
                val separator = AmitySettingsItem.Separator
                if (hasEditPermission) {
                    val postReviewApprovalMenu = menuCreator.createAllowCommentMenu(
                        getNeededAllowCommentDataSource(communityId!!)
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
        return updateAllowComment(communityId!!, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    fun turnOff(onError: () -> Unit): Completable {
        return updateAllowComment(communityId!!, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError.invoke()
            }
            .ignoreElement()
    }

    private fun getNeededAllowCommentDataSource(communityId: String): Flowable<Boolean> {
        return Flowable.combineLatest(
            getNeededSettings(communityId),
            getReversionSource().startWith(Single.just(false))
        ) { isToggled, _ -> isToggled }
    }

    private fun getNeededSettings(communityId: String): Flowable<Boolean> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .map { it.getStorySettings().allowComment }
    }

    private fun getReversionSource(): Flowable<Boolean> {
        return isToggleState.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun revertToggleState() {
        isToggleState.onNext(true)
    }

    private fun updateAllowComment(
        communityId: String,
        isAllowed: Boolean
    ): Single<AmityCommunity> {
        val storySettings = AmityCommunityStorySettings(allowComment = isAllowed)
        return AmitySocialClient.newCommunityRepository()
            .editCommunity(communityId)
            .storySettings(storySettings)
            .build()
            .apply()
    }
}


private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"
package com.amity.socialcloud.uikit.community.compose.community.setting

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityStorySettings
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityCommunitySettingPageViewModel(override val communityId: String) :
    AmityCommunityNotificationViewModel(communityId) {

    private val _uiState by lazy {
        MutableStateFlow<AmityCommunitySettingUIState>(AmityCommunitySettingUIState.Initial)
    }
    val uiState get() = _uiState

    fun updateUIEvent(uiEvent: AmityCommunitySettingUIEvent) {
        when (uiEvent) {
            AmityCommunitySettingUIEvent.ConfirmModeratorLeaveCommunity -> {
                updateUIState(AmityCommunitySettingUIState.ShowConfirmModeratorLeaveDialog)
            }

            AmityCommunitySettingUIEvent.ConfirmUserLeaveCommunity -> {
                updateUIState(AmityCommunitySettingUIState.ShowConfirmUserLeaveDialog)
            }

            AmityCommunitySettingUIEvent.ConfirmLastMemberLeaveCommunity -> {
                updateUIState(AmityCommunitySettingUIState.ShowUserIsLastMemberDialog)
            }

            AmityCommunitySettingUIEvent.ProceedClosingCommunity -> {
                closeCommunity(
                    onSuccess = {
                        updateUIState(AmityCommunitySettingUIState.CloseCommunitySuccess)
                    },
                    onError = {
                        updateUIState(AmityCommunitySettingUIState.CloseCommunityFailed(it))
                    }
                )
            }

            AmityCommunitySettingUIEvent.ProceedLeavingCommunity -> {
                leaveCommunity(
                    onSuccess = {
                        updateUIState(AmityCommunitySettingUIState.LeaveCommunitySuccess)
                    },
                    onError = {
                        updateUIState(AmityCommunitySettingUIState.LeaveCommunityFailed(it))
                    }
                )
            }

            AmityCommunitySettingUIEvent.Initial -> {}
        }
    }

    fun updateUIState(uiState: AmityCommunitySettingUIState) {
        viewModelScope.launch {
            _uiState.value = uiState
            delay(300)
            _uiState.value = AmityCommunitySettingUIState.Initial
        }
    }

    fun hasEditPermission(): Flowable<Boolean> {
        return hasPermissionAtCommunity(AmityPermission.EDIT_COMMUNITY, communityId)
    }

    fun hasDeletePermission(): Flowable<Boolean> {
        return hasPermissionAtCommunity(AmityPermission.DELETE_COMMUNITY, communityId)
    }

    fun hasReviewPermission(): Flowable<Boolean> {
        return hasPermissionAtCommunity(AmityPermission.REVIEW_COMMUNITY_POST, communityId)
    }

    fun updatePostSetting(
        setting: AmityCommunityPostSettings,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .editCommunity(communityId)
            .postSettings(setting)
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { onSuccess() }
            .doOnError { onError.invoke(AmityError.from(it)) }
            .subscribe()
    }

    fun updateStorySetting(
        setting: AmityCommunityStorySettings,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .editCommunity(communityId)
            .storySettings(setting)
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { onSuccess() }
            .doOnError { onError.invoke(AmityError.from(it)) }
            .subscribe()
    }

    private fun leaveCommunity(
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .leaveCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError.invoke(AmityError.from(it)) }
            .subscribe()
    }

    private fun closeCommunity(
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .deleteCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError.invoke(AmityError.from(it)) }
            .subscribe()
    }

    fun getCommunity(): Flowable<AmityCommunity> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

sealed class AmityCommunitySettingUIEvent {
    object Initial : AmityCommunitySettingUIEvent()
    object ConfirmUserLeaveCommunity : AmityCommunitySettingUIEvent()
    object ConfirmModeratorLeaveCommunity : AmityCommunitySettingUIEvent()
    object ConfirmLastMemberLeaveCommunity : AmityCommunitySettingUIEvent()
    object ProceedLeavingCommunity : AmityCommunitySettingUIEvent()
    object ProceedClosingCommunity : AmityCommunitySettingUIEvent()
}

sealed class AmityCommunitySettingUIState {
    object Initial : AmityCommunitySettingUIState()
    object ShowConfirmUserLeaveDialog : AmityCommunitySettingUIState()
    object ShowConfirmModeratorLeaveDialog : AmityCommunitySettingUIState()
    object ShowUserUnableToLeaveDialog : AmityCommunitySettingUIState()
    object ShowUserIsLastModeratorDialog : AmityCommunitySettingUIState()
    object ShowUserIsLastMemberDialog : AmityCommunitySettingUIState()
    object LeaveCommunitySuccess : AmityCommunitySettingUIState()
    data class LeaveCommunityFailed(val error: AmityError) : AmityCommunitySettingUIState()
    object CloseCommunitySuccess : AmityCommunitySettingUIState()
    data class CloseCommunityFailed(val error: AmityError) : AmityCommunitySettingUIState()
}
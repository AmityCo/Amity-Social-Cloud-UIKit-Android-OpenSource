package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class AmityCreatePostMenuComponentViewModel : AmityBaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        checkStoryPermissions()
        checkEventPermissions()
    }

    private fun checkStoryPermissions() {
        // Combine global setting and permission check
        val globalSetting = AmitySocialClient.getSettings()
            .map { it.getStorySettings().isAllowAllUserToCreateStory() }
            .subscribeOn(Schedulers.io())

        val globalPermission = AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY)
            .atGlobal()
            .check()
            .subscribeOn(Schedulers.io())

        disposables.add(
            Flowable.combineLatest(
                globalSetting,
                globalPermission
            ) { isGloballyAllowed, hasPermission ->
                isGloballyAllowed || hasPermission
            }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { shouldShow ->
                    _uiState.value = _uiState.value.copy(canCreateStory = shouldShow)
                }
                .doOnError { }
                .subscribe()
        )
    }

    private fun checkEventPermissions() {
        if (!AmityCoreClient.isSignedIn()) {
            _uiState.value = _uiState.value.copy(canCreateEvent = false)
        } else {
            AmityCoreClient.hasPermission(AmityPermission.CREATE_EVENT)
                .atGlobal()
                .check()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    _uiState.value = _uiState.value.copy(canCreateEvent = it)
                }
                .subscribe()
                .let(::addDisposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    data class UiState (
        val canCreateStory: Boolean = false,
        val canCreateEvent: Boolean = false
    )
}

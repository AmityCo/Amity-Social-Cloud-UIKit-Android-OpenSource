package com.amity.socialcloud.uikit.community.compose.socialhome.components

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AmityCreatePostMenuComponentViewModel : AmityBaseViewModel() {

    private val _showStoryAction = MutableStateFlow(false)
    val showStoryAction = _showStoryAction.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        checkStoryPermissions()
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
                    _showStoryAction.value = shouldShow
                }
                .doOnError { }
                .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

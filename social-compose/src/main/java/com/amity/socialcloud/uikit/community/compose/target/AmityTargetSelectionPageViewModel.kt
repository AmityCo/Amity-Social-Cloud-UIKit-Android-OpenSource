package com.amity.socialcloud.uikit.community.compose.target

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetContentType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityTargetSelectionPageViewModel : AmityBaseViewModel() {
    private val _contentType = MutableStateFlow<AmityTargetContentType?>(null)
    val allowToCreateMap = MutableStateFlow(mapOf<String, Boolean>())
    private val disposables = CompositeDisposable()
    // Track which communities we've already checked to avoid duplicate checks
    private val checkedCommunities = mutableSetOf<String>()
    private val _allowAllUserStoryCreation = MutableStateFlow(false)


    fun setContentType(type: AmityTargetContentType) {
        _contentType.value = type
        allowToCreateMap.value = emptyMap()
        checkedCommunities.clear()

        // Check story settings when content type is STORY
        if (type == AmityTargetContentType.STORY) {
            disposables.add(
                AmitySocialClient.getSettings()
                    .map { it.getStorySettings().isAllowAllUserToCreateStory() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { isAllowed ->
                        _allowAllUserStoryCreation.value = isAllowed
                    }
                    .doOnError { }
                    .subscribe()
            )
        }
    }


    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCommunities(): Flow<PagingData<AmityCommunity>> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunities()
            .filter(AmityCommunityFilter.MEMBER)
            .build()
            .query()
            .asFlow()
            .catch {}
    }

    fun checkPermissionIfNeeded(community: AmityCommunity) {
        val communityId = community.getCommunityId()
        if (communityId in checkedCommunities) return

        checkedCommunities.add(communityId)

        when (_contentType.value) {
            AmityTargetContentType.POST -> {
                if (community.getPostSettings() != AmityCommunityPostSettings.ADMIN_CAN_POST_ONLY) {
                    setAllowToCreate(communityId, true)
                } else {
                    checkPostPermission(communityId)
                }
            }

            AmityTargetContentType.STORY -> {
                if (_allowAllUserStoryCreation.value) {
                    setAllowToCreate(communityId, true)
                } else {
                    checkStoryPermission(communityId)
                }
            }

            null -> {}
        }
    }

    private fun checkPostPermission(communityId: String) {
        AmityCoreClient.hasPermission(AmityPermission.CREATE_PRIVILEGED_POST)
            .atCommunity(communityId)
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hasPermission -> setAllowToCreate(communityId, hasPermission)}
            .doOnError {  }
            .subscribe()
            .let { disposables.add(it) }
    }

    private fun checkStoryPermission(communityId: String) {
        AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY)
            .atCommunity(communityId)
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hasPermission -> setAllowToCreate(communityId, hasPermission)}
            .doOnError {  }
            .subscribe()
            .let { disposables.add(it) }
    }

    private fun setAllowToCreate(communityId: String, isAllowed: Boolean) {
        allowToCreateMap.value += (communityId to isAllowed)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
package com.amity.socialcloud.uikit.community.compose.target

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityTargetSelectionPageViewModel : AmityBaseViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()
    val allowToCreateMap = MutableStateFlow(mapOf<String, Boolean>())

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

    fun handlePermissionCheck(community: AmityCommunity) {
        AmityCoreClient
            .hasPermission(AmityPermission.CREATE_PRIVILEGED_POST)
            .atCommunity(community.getCommunityId())
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (!allowToCreateMap.value.containsKey(community.getCommunityId())
                    || allowToCreateMap.value[community.getCommunityId()] != it
                ) {
                    setAllowToCreate(community.getCommunityId(), it)
                }
            }
            .doOnError {  }
            .subscribe()
            .let { disposables.add(it) }
    }

    fun setAllowToCreate(communityId: String, isAllow: Boolean) {
        allowToCreateMap.value = allowToCreateMap.value.plus(Pair(communityId,isAllow))
    }
}
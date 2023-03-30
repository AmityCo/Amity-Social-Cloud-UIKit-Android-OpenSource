package com.amity.socialcloud.uikit.community.detailpage

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.PermissionViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers


class AmityCommunityDetailViewModel(private val savedState: SavedStateHandle): AmityBaseViewModel(), PermissionViewModel {

    var communityId: String? = null
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
    }


     fun getCommunity(onLoaded: (AmityCommunity) -> Unit): Completable {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onLoaded.invoke(it)
            }.ignoreElements()
    }

}

private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"

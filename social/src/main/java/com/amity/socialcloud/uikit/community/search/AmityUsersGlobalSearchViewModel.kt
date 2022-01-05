package com.amity.socialcloud.uikit.community.search

import androidx.databinding.ObservableField
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserSortOption
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityUsersGlobalSearchViewModel: AmityBaseViewModel() {

    var searchString = ObservableField("")

    fun searchUsers(onResult: (list: PagedList<AmityUser>) -> Unit): Completable {
        val userRepository = AmityCoreClient.newUserRepository()
        return userRepository.searchUserByDisplayName(searchString.get() ?:  "")
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    fun setPropertyChangeCallback() {
        searchString.addOnPropertyChanged {
            triggerEvent(AmityEventIdentifier.SEARCH_USERS)
        }
    }
}
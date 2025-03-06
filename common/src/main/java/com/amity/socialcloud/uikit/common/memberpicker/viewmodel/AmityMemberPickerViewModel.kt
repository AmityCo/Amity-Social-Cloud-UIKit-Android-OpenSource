package com.amity.socialcloud.uikit.common.memberpicker.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityMemberPickerViewModel : AmityBaseViewModel() {

    val searchString = ObservableField("")
    val selectedMembersList: ArrayList<AmitySelectMemberItem> = arrayListOf()
    val selectedMemberSet: HashSet<String> = hashSetOf()
    val memberMap: HashMap<String, Int> = hashMapOf()
    val searchMemberMap: HashMap<String, Int> = hashMapOf()
    val isSearchUser = ObservableBoolean(false)
    val leftString = MutableLiveData<String>("")
    val rightStringActive = MutableLiveData<Boolean>(false)

    @OptIn(ExperimentalPagingApi::class)
    fun getAllUsers(): Flowable<PagingData<AmityUser>> {
        val userRepo = AmityCoreClient.newUserRepository()
        return userRepo.searchUsers("")
            .build()
            .query()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun searchUser(onResult: (list: PagingData<AmityUser>) -> Unit): Completable {
        val userRepo = AmityCoreClient.newUserRepository()
        return userRepo.searchUsers(searchString.get() ?: "")
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

    fun prepareSelectedMembersList(memberAmity: AmitySelectMemberItem, isSelected: Boolean) {
        if (isSelected) {
            selectedMembersList.add(memberAmity)
        } else {
            selectedMembersList.remove(memberAmity)
        }
    }

    fun setPropertyChangeCallback() {
        searchString.addOnPropertyChanged {
            triggerEvent(AmityEventIdentifier.SEARCH_STRING_CHANGED)
        }
    }
}
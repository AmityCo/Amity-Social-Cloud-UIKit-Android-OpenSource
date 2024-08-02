package com.amity.socialcloud.uikit.community.members


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.member.query.AmityCommunityMembershipSortOption
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembershipFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunityMembersViewModel : AmityBaseViewModel() {

    var communityId: String = ""
    var community: AmityCommunity? = null
    val searchString = ObservableField("")
    val selectMembersList = ArrayList<AmitySelectMemberItem>()
    val membersSet = HashSet<String>()
    val isJoined = ObservableBoolean(false)
    val isModerator = ObservableBoolean(false)
    val addRemoveErrorData = MutableLiveData<Throwable>()


    fun isCommunityLoaded(): Boolean = (community != null)

    fun getCommunityMembers(
        onCommunityLoaded: (AmityCommunity) -> Unit,
        onMembersLoaded: (PagingData<AmityCommunityMember>) -> Unit,
        onMembersLoadFailed: () -> Unit
    ): Completable {
        return getCommunity()
            .doOnNext {
                isJoined.set(it.isJoined())
                communityId = it.getCommunityId()
                community = it
                onCommunityLoaded.invoke(it)
            }
            .firstOrError()
            .flatMapPublisher {
                AmitySocialClient.newCommunityRepository()
                    .membership(communityId)
                    .getMembers()
                    .filter(AmityCommunityMembershipFilter.MEMBER)
                    .includeDeleted(false)
                    .build()
                    .query()
            }
            .doOnNext { onMembersLoaded.invoke(it) }
            .doOnError { onMembersLoadFailed.invoke() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElements()
    }

    private fun getCommunity(): Flowable<AmityCommunity> {
        if (community != null) {
            return Flowable.just(community!!)
        }
        return AmitySocialClient.newCommunityRepository().getCommunity(communityId)
            .subscribeOn(Schedulers.io())
    }

    fun checkModeratorPermission(hasPermission: (Boolean) -> Unit): Completable {
        return Flowable.combineLatest(
            getCommunity(),
            hasPermissionAtCommunity(AmityPermission.EDIT_COMMUNITY_USER, communityId),
            BiFunction { community, hasEditPermission ->
                if (community.isJoined()) {
                    if (AmityCoreClient.getUserId() == community.getCreatorId()) {
                        return@BiFunction true
                    } else {
                        return@BiFunction hasEditPermission
                    }
                } else false
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .firstOrError()
            .doOnSuccess { hasPermission.invoke(it) }
            .doOnError { hasPermission.invoke(false) }
            .ignoreElement()
    }

    fun getCommunityModerators(onModeratorsLoaded: (PagingData<AmityCommunityMember>) -> Unit): Completable {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .getMembers()
            .filter(AmityCommunityMembershipFilter.MEMBER)
            .sortBy(AmityCommunityMembershipSortOption.FIRST_CREATED)
            .roles(listOf(AmityConstants.CHANNEL_MODERATOR_ROLE, AmityConstants.COMMUNITY_MODERATOR_ROLE))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onModeratorsLoaded.invoke(it) }
            .doOnError { }
            .ignoreElements()
    }

    fun handleAddRemoveMembers(
        newList: ArrayList<AmitySelectMemberItem>,
        onMembersAdded: () -> Unit,
        onFailed: () -> Unit
    ) {
        val addedMembers = arrayListOf<String>()
        val removedMembers = arrayListOf<String>()
        val toRemoveMembers = arrayListOf<AmitySelectMemberItem>()
        for (item in selectMembersList) {
            if (newList.contains(item)) {
                newList.remove(item)
            } else {
                removedMembers.add(item.id)
                toRemoveMembers.add(item)
            }
        }
        for (item in newList) {
            addedMembers.add(item.id)
        }
        if (removedMembers.isNotEmpty()) {
            removeUsersFromCommunity(removedMembers)
        }
        if (addedMembers.isNotEmpty()) {
            addMembersToCommunity(
                addedMembers,
                onMembersAdded = onMembersAdded,
                onFailed = onFailed
            )
        }

        for (member in toRemoveMembers) {
            updateSelectedMembersList(member)
        }
    }

    private fun removeUsersFromCommunity(list: List<String>) {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        addDisposable(
            communityRepository.membership(communityId)
                .removeMembers(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {

                }.doOnError {
                    if (addRemoveErrorData.value == null) {
                        addRemoveErrorData.value = it
                    }
                }
                .subscribe()
        )
    }

    private fun addMembersToCommunity(
        list: List<String>,
        onMembersAdded: () -> Unit,
        onFailed: () -> Unit
    ) {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        addDisposable(
            communityRepository.membership(communityId)
                .addMembers(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    onMembersAdded.invoke()
                }.doOnError {
                    onFailed.invoke()
                    if (addRemoveErrorData.value == null) {
                        addRemoveErrorData.value = it
                    }
                }
                .subscribe()
        )
    }

    fun updateSelectedMembersList(memberAmity: AmitySelectMemberItem) {
        selectMembersList.remove(memberAmity)
        membersSet.remove(memberAmity.id)
    }
}
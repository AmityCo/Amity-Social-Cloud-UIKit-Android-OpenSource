package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class AmityMentionViewModel : AmityBaseViewModel() {

    fun searchUsersMention(
        keyword: String,
    ): Flow<PagingData<AmityUser>> {
        return AmityCoreClient.newUserRepository()
            .searchUsers(keyword)
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun searchCommunityUsersMention(
        communityId: String,
        keyword: String,
    ): Flow<PagingData<AmityUser>> {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityCommunityMembership.MEMBER))
            .includeDeleted(false)
            .build()
            .query()
            .map { pagingData ->
                pagingData.map { communityMember ->
                    communityMember.getUser()!!
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
}
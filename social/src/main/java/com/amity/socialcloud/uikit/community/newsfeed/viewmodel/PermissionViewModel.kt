package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import io.reactivex.rxjava3.core.Flowable

interface PermissionViewModel {

    fun hasCommunityPermission(
        communitySource: Flowable<AmityCommunity>,
        permissionSource: Flowable<Boolean>
    ): Flowable<Boolean> {
        return Flowable.combineLatest(
            permissionSource, communitySource
        ) { hasPermission: Boolean, community ->
            if (community.isJoined() && AmityCoreClient.getUserId() == community.getCreatorId()) {
                true
            } else {
                hasPermission
            }
        }
    }

    fun getCommunityPermissionSource(
        communityId: String,
        permission: AmityPermission
    ): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(permission)
            .atCommunity(communityId)
            .check()
    }

}
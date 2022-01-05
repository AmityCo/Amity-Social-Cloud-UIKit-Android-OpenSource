package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import io.reactivex.Flowable

interface PermissionViewModel {

    fun hasCommunityPermission(
        communitySource: Flowable<AmityCommunity>,
        permissionSource: Flowable<Boolean>
    ): Flowable<Boolean> {
        return Flowable.combineLatest(permissionSource, communitySource,
            { hasPermission: Boolean, community ->
                if (community.isJoined() && AmityCoreClient.getUserId() == community.getUserId()) {
					true
                } else {
                	hasPermission
				}
            })
    }

    fun getCommunityPermissionSource(communityId: String, permission: AmityPermission) : Flowable<Boolean>{
        return AmityCoreClient.hasPermission(permission)
            .atCommunity(communityId)
            .check()
    }

}
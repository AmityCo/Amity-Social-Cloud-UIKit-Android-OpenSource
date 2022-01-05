package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityUser
import io.reactivex.Flowable

interface UserViewModel {

    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
    }

}
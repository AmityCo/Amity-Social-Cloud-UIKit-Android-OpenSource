package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUserType

fun AmityCoreClient.isVisitor(): Boolean {
    return AmityCoreClient.getCurrentUserType() == AmityUserType.VISITOR
}

fun AmityCoreClient.isSignedIn(): Boolean {
    return AmityCoreClient.getCurrentUserType() == AmityUserType.SIGNED_IN
}
package com.amity.socialcloud.uikit.community.newsfeed.model

sealed class AmityMediaGalleryTarget(val id: String) {
    internal abstract fun getName(): String
    class USER(val userId: String) : AmityMediaGalleryTarget(userId) {
        override fun getName() = TARGET_USER
    }

    class COMMUNITY(val communityId: String) : AmityMediaGalleryTarget(communityId) {
        override fun getName() = TARGET_COMMUNITY
    }
}


internal const val TARGET_USER = "user"
internal const val TARGET_COMMUNITY = "community"
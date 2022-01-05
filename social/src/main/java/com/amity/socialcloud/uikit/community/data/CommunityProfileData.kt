package com.amity.socialcloud.uikit.community.data

import com.amity.socialcloud.sdk.social.community.AmityCommunity

class CommunityProfileData(
    val community: AmityCommunity,
    val canEditCommunity: Boolean,
    val postReviewBanner: PostReviewBannerData
) {
}
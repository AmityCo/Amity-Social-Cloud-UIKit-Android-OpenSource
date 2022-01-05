package com.amity.socialcloud.uikit.community.members

import com.amity.socialcloud.sdk.social.community.AmityCommunityMember

interface AmityMemberClickListener {

    fun onCommunityMembershipSelected(membership: AmityCommunityMember)
}
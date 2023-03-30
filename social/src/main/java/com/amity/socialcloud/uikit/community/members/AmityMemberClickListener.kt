package com.amity.socialcloud.uikit.community.members

import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember

interface AmityMemberClickListener {

    fun onCommunityMembershipSelected(membership: AmityCommunityMember)
}
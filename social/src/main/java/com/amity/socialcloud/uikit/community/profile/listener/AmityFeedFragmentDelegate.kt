package com.amity.socialcloud.uikit.community.profile.listener

import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityFeedFragment

interface AmityFeedFragmentDelegate {
    fun getFeedFragment(): AmityFeedFragment
}
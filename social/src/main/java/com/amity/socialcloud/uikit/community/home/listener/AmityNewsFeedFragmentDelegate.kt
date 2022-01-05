package com.amity.socialcloud.uikit.community.home.listener

import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityNewsFeedFragment

interface AmityNewsFeedFragmentDelegate {
    fun getNewsFeedFragment(): AmityNewsFeedFragment
}
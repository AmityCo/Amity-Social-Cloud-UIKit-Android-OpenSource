package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.feed.AmityPost

interface AmityPostItemListener {

    fun onClickComment(post: AmityPost, fragment: Fragment)

}
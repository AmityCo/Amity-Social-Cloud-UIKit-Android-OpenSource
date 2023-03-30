package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.post.AmityPost

interface AmityPostItemListener {

    fun onClickComment(post: AmityPost, fragment: Fragment)

}
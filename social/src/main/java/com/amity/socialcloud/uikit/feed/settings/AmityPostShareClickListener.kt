package com.amity.socialcloud.uikit.feed.settings

import android.content.Context
import com.amity.socialcloud.sdk.social.feed.AmityPost

interface AmityPostShareClickListener {

    fun shareToMyTimeline(context: Context, post: AmityPost) {

    }

    fun shareToGroup(context: Context, post: AmityPost) {

    }

    fun shareToExternal(context: Context, post: AmityPost) {

    }

}
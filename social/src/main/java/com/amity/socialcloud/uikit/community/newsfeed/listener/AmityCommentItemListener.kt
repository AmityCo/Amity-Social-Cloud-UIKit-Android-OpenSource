package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.comment.AmityComment

interface AmityCommentItemListener {

    fun onClickReply(comment: AmityComment, fragment: Fragment)

}
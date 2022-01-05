package com.amity.socialcloud.uikit.community.newsfeed.listener

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.amity.socialcloud.uikit.community.utils.AmityCommunityNavigation

open class AmityMentionClickableSpan() : ClickableSpan() {

    private var userId: String = ""

    constructor(userId: String) : this() {
        this.userId = userId
    }

    override fun onClick(widget: View) {
        AmityCommunityNavigation
            .navigateToUserProfile(widget.context, userId)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}
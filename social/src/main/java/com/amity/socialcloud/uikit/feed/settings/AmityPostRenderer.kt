package com.amity.socialcloud.uikit.feed.settings

import android.view.View
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostContentViewHolder

interface AmityPostRenderer {

    fun getDataType(): String

    fun getLayoutId(): Int

    fun createViewHolder(view: View): AmityPostContentViewHolder

    fun enableHeader(): Boolean

    fun enableFooter(): Boolean

}
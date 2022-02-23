package com.amity.socialcloud.uikit.community.views.createpost

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StyleRes
import com.amity.socialcloud.uikit.common.common.views.AmityStyle
import com.amity.socialcloud.uikit.community.R

class AmityPostComposeViewStyle : AmityStyle {
    var backgroundColor: Int = -1
    var padding: Int = -1
    var hint: Int = -1
    var mentionColor: Int = -1

    init {
        backgroundColor = getColor(android.R.color.transparent)
        padding = getDimensionPixelSize(R.dimen.amity_padding_xs)
        hint = R.string.amity_post_compose_hint
        mentionColor =  android.R.color.black
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context)
    constructor(context: Context, @StyleRes style: Int) : super(context)

}
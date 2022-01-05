package com.amity.socialcloud.uikit.common.common.views.avatar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleRes
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.views.AmityStyle

class AmityAvatarViewStyle : AmityStyle {
    var avatarHeight: Int = -1
    var avatarWidth: Int = -1
    var avatarShape: Int = -1
    var avatarDrawable: Int = -1
    var avatarUrl: String? = null

    constructor(context: Context, attributeSet: AttributeSet) : super(context) {
        parseStyle(context, attributeSet)
    }

    constructor(context: Context, @StyleRes style: Int) : super(context) {
        parseStyle(context, style)
    }

    private fun parseStyle(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AmityAvatar)
        parse(typedArray)
    }

    private fun parseStyle(context: Context, @StyleRes style: Int) {
        val typeArray = context.obtainStyledAttributes(style, R.styleable.AmityAvatar);
        parse(typeArray)
    }

    private fun parse(typedArray: TypedArray) {
        avatarHeight = typedArray.getDimensionPixelSize(
            R.styleable.AmityAvatar_amityAvatarHeight,
            getDimensionPixelSize(R.dimen.amity_avatar_default_height)
        )
        avatarWidth = typedArray.getDimensionPixelSize(
            R.styleable.AmityAvatar_amityAvatarWidth,
            getDimensionPixelSize(R.dimen.amity_avatar_default_width)
        )
        avatarShape = typedArray.getInt(R.styleable.AmityAvatar_amityAvatarShape, AmityAvatarShape.Circle)
        avatarDrawable = typedArray.getResourceId(R.styleable.AmityAvatar_amityAvatarDrawable, -1);
        avatarUrl = typedArray.getString(R.styleable.AmityAvatar_amityAvatarUrl)
        typedArray.recycle()

    }
}

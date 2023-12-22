package com.amity.socialcloud.uikit.common.config

import androidx.annotation.DrawableRes
import com.amity.socialcloud.uikit.common.R

object AmityUIKitDrawableResolver {

    private val MAPPER = listOf(
        "close.png" to R.drawable.amity_ic_close,
        "lock.png" to R.drawable.amity_ic_lock1,
        "threeDot.png" to R.drawable.amity_ic_more_horiz,
        "back.png" to R.drawable.amity_ic_arrow_back,
        "aspect_ratio.png" to R.drawable.amity_ic_aspect_ratio,
        "share_story_button.png" to R.drawable.amity_ic_arrow_forward
    )

    @DrawableRes
    fun getDrawableRes(name: String): Int {
        return MAPPER.firstOrNull { it.first == name }?.second ?: R.drawable.amity_ic_warning
    }
}
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
        "share_story_button.png" to R.drawable.amity_ic_arrow_forward,
        "hyperlink_button.png" to R.drawable.amity_ic_hyperlink,
        "message_reaction_heart.png" to R.drawable.amity_ic_reaction_heart,
        "message_reaction_like.png" to R.drawable.amity_ic_reaction_like,
        "message_reaction_fire.png" to R.drawable.amity_ic_reaction_fire,
        "message_reaction_grinning.png" to R.drawable.amity_ic_reaction_grinning,
        "message_reaction_sad.png" to R.drawable.amity_ic_reaction_sad,
    )

    @DrawableRes
    fun getDrawableRes(name: String): Int {
        return MAPPER.firstOrNull { it.first == name }?.second ?: R.drawable.amity_ic_warning
    }
}
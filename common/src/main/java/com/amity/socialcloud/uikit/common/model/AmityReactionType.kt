package com.amity.socialcloud.uikit.common.model

import androidx.annotation.DrawableRes
import com.amity.socialcloud.uikit.common.R

data class AmityReactionType(
	val name: String,
	@DrawableRes val icon: Int,
) {
	companion object {
		val UNKNOWN : AmityReactionType = AmityReactionType(
			name = "unknown",
			icon = R.drawable.amity_ic_reaction_like // Placeholder for unknown reaction icon
		)
	}
}
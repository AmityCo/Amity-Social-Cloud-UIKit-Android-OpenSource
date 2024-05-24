package com.amity.socialcloud.uikit.common.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object AmityNumberUtil {
	fun getNumberAbbreveation(number: Int): String {
		val format = DecimalFormat("#.#")
		format.roundingMode = RoundingMode.HALF_UP
		return when {
			number < 1000 -> number.toString()
			number < 1000000 -> "${format.format(number / 1000f)}K"
			else -> "${format.format(number / 1000000f)}M"
		}
	}
}
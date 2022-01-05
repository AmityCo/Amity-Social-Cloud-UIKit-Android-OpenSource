package com.amity.socialcloud.uikit.common.common.views

import androidx.core.graphics.ColorUtils
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade.*

object AmityColorPaletteUtil {
    private val colorMap = HashMap<Int, HashMap<AmityColorShade, Int>>()

    fun getColor(color: Int, shade: AmityColorShade): Int {
        if (colorMap[color]?.contains(shade) == true) {
            return colorMap[color]!![shade]!!
        }

        val hslColor = FloatArray(3)
        ColorUtils.colorToHSL(color, hslColor)
        hslColor[2] = hslColor[2] + getLumenValue(
            shade
        )

        val calculatedColor = ColorUtils.HSLToColor(hslColor)

        val colors = colorMap[color] ?: hashMapOf()
        colors[shade] = calculatedColor
        colorMap[color] = colors
        return calculatedColor

    }

    private fun getLumenValue(shade: AmityColorShade): Float {
        return (
                when (shade) {
                    SHADE1 -> 0.25F
                    SHADE2 -> 0.40F
                    SHADE3 -> 0.50F
                    SHADE4 -> 0.75F
                    else -> 0.0F
                })
    }
}
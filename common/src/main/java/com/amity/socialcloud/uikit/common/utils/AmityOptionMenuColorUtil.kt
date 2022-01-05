package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade

class AmityOptionMenuColorUtil {

    companion object {
        fun getColor(enabled: Boolean, context: Context): Int {
            return if (enabled) {
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(
                        context,
                        com.amity.socialcloud.uikit.common.R.color.amityColorHighlight
                    ), AmityColorShade.DEFAULT
                )
            } else {

                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(
                        context,
                        com.amity.socialcloud.uikit.common.R.color.amityColorHighlight
                    ), AmityColorShade.SHADE2
                )
            }
        }
    }

}
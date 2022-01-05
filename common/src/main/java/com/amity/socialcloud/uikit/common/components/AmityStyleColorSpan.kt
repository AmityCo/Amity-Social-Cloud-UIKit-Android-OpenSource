package com.amity.socialcloud.uikit.common.components

import android.text.TextPaint
import android.text.style.StyleSpan
import androidx.annotation.ColorInt

class AmityStyleColorSpan(@ColorInt private val color: Int, style: Int) : StyleSpan(style) {

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.color = color
    }

    override fun updateMeasureState(paint: TextPaint) {
        super.updateMeasureState(paint)
        paint.color = color
    }
}
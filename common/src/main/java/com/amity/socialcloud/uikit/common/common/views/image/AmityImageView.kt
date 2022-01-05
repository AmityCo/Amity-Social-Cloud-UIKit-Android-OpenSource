package com.amity.socialcloud.uikit.common.common.views.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R

class AmityImageView : AppCompatImageView {

    lateinit var style: AmityImageViewStyle

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        parseStyle(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        parseStyle(attrs)
    }


    private fun parseStyle(attrs: AttributeSet) {
        style = AmityImageViewStyle(context, attrs)
        applyStyle()
    }

    fun setViewStyle(style: AmityImageViewStyle) {
        this.style = style
        applyStyle()
    }

    private fun applyStyle() {
        if (style.tintColor != -1) {
            setColorFilter(
                ContextCompat.getColor(context, R.color.amityColorAlert),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
        }

    }
}
package com.amity.socialcloud.uikit.common.common.views.text

import android.content.Context
import android.util.AttributeSet

class AmityTextView : androidx.appcompat.widget.AppCompatTextView {

    lateinit var style: AmityTextStyle


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
        style = AmityTextStyle(context, attrs)
        applyStyle()
    }

    fun setViewStyle(style: AmityTextStyle) {
        this.style = style
        applyStyle()
    }

    private fun applyStyle() {
        setTextColor(style.textColor)
        setTypeface(typeface, style.textStyle)
    }
}
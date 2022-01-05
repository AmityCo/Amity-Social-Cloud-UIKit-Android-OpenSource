package com.amity.socialcloud.uikit.common.common.views.text

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.StyleRes
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.views.AmityStyle

class AmityTextStyle : AmityStyle {

    var textColor: Int = -1
    var textStyle: Int = -1

    constructor(context: Context, attributeSet: AttributeSet) : super(context) {
        parseStyle(context, attributeSet)
    }

    constructor(context: Context, @StyleRes style: Int) : super(context) {
        parseStyle(context, style)
    }


    private fun parseStyle(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AmityText)
        parse(typedArray)
    }

    private fun parseStyle(context: Context, @StyleRes style: Int) {
        val typeArray = context.obtainStyledAttributes(style, R.styleable.AmityText);
        parse(typeArray)
    }

    private fun parse(typedArray: TypedArray) {
        textColor = typedArray.getColor(
            R.styleable.AmityText_amityTextColor,
            getColor(R.color.amityColorBase)
        )
        textStyle = typedArray.getInt(
            R.styleable.AmityText_amityTextStyle,
            Typeface.NORMAL
        )

        typedArray.recycle()

    }


}
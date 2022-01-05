package com.amity.socialcloud.uikit.common.common.views.button

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleRes
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.views.AmityStyle

class AmityButtonStyle : AmityStyle {
    var outlineColor: Int = -1
    var backgroundColor: Int = -1
    var buttonType: Int = AmityButtonType.Solid
    var buttonTextColor: Int = -1

    constructor(context: Context, attributeSet: AttributeSet) : super(context) {
        parseStyle(context, attributeSet)
    }

    constructor(context: Context, @StyleRes style: Int) : super(context) {
        parseStyle(context, style)
    }


    private fun parseStyle(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AmityButton)
        parse(typedArray)
    }

    private fun parseStyle(context: Context, @StyleRes style: Int) {
        val typeArray = context.obtainStyledAttributes(style, R.styleable.AmityButton);
        parse(typeArray)
    }

    private fun parse(typedArray: TypedArray) {
        outlineColor = typedArray.getColor(
            R.styleable.AmityButton_amityOutlineColor,
            getColor(R.color.amityColorBase)
        )

        backgroundColor = typedArray.getColor(
            R.styleable.AmityButton_amityBackgroundColor,
            getColor(R.color.amityColorBlack)
        )

        buttonType = typedArray.getInt(R.styleable.AmityButton_amityButtonType, AmityButtonType.Solid)
        buttonTextColor =
            typedArray.getInt(R.styleable.AmityButton_amityButtonTextColor, getColor(R.color.amityColorBase))

        typedArray.recycle()

    }
}

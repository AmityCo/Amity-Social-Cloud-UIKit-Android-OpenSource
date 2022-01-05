package com.amity.socialcloud.uikit.common.common.views.image

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleRes
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.views.AmityStyle

class AmityImageViewStyle : AmityStyle {

    var tintColor: Int = -1

    constructor(context: Context, attributeSet: AttributeSet) : super(context) {
        parseStyle(context, attributeSet)
    }

    constructor(context: Context, @StyleRes style: Int) : super(context) {
        parseStyle(context, style)
    }


    private fun parseStyle(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AmityImage)
        parse(typedArray)
    }

    private fun parseStyle(context: Context, @StyleRes style: Int) {
        val typeArray = context.obtainStyledAttributes(style, R.styleable.AmityImage);
        parse(typeArray)
    }

    private fun parse(typedArray: TypedArray) {
        tintColor = typedArray.getColor(
            R.styleable.AmityImage_amityTintColor,
            -1
        )

        typedArray.recycle()

    }

}
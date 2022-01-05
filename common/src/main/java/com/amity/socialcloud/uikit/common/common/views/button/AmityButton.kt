package com.amity.socialcloud.uikit.common.common.views.button

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.setPadding
import com.amity.socialcloud.uikit.common.R

class AmityButton : AppCompatButton {

    lateinit var buttonStyle: AmityButtonStyle


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
        buttonStyle = AmityButtonStyle(context, attrs)
        applyStyle()
    }

    fun setViewStyle(buttonStyle: AmityButtonStyle) {
        this.buttonStyle = buttonStyle
        applyStyle()
    }

    private fun applyStyle() {
        if (buttonStyle.backgroundColor != null) {
            setBackgroundColor(buttonStyle.backgroundColor)
        }

        setTextColor(buttonStyle.buttonTextColor)

        setPadding(context.resources.getDimensionPixelSize(R.dimen.amity_padding_xs))


    }
}
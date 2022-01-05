package com.amity.socialcloud.uikit.common.common.views

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R

open class AmityStyle {

    protected var context: Context
    private var resources: Resources

    constructor(
        context: Context
    ) {
        this.context = context
        resources = context.resources
    }

    protected fun getSystemAccentColor(): Int {
        return getSystemColor(R.attr.colorAccent)
    }

    protected fun getSystemPrimaryColor(): Int {
        return getSystemColor(R.attr.colorPrimary)
    }

    protected fun getSystemPrimaryDarkColor(): Int {
        return getSystemColor(R.attr.colorPrimaryDark)
    }

    protected fun getSystemPrimaryTextColor(): Int {
        return getSystemColor(android.R.attr.textColorPrimary)
    }

    protected fun getSystemHintColor(): Int {
        return getSystemColor(android.R.attr.textColorHint)
    }

    protected fun getSystemColor(@AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(attr))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }

    protected fun getDimensionPixelSize(@DimenRes dimen: Int): Int {
        return resources.getDimensionPixelSize(dimen)
    }

    protected fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    protected fun getDrawable(@DrawableRes drawable: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawable)
    }

    protected fun getVectorDrawable(@DrawableRes drawable: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawable)
    }
}
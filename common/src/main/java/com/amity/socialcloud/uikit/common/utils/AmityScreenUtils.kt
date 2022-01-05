package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager


object AmityScreenUtils {
    fun getScreenHeight(context: Context): Int = getScreenWidthHeight(context).y

    fun getScreenWidth(context: Context): Int = getScreenWidthHeight(context).x

    fun getHalfScreenHeight(context: Context): Int = getScreenWidthHeight(context).y / 2

    fun getHalfScreenWidth(context: Context): Int = getScreenWidthHeight(context).x / 2

    fun getOneThirdScreenHeight(context: Context): Int = getScreenWidthHeight(context).y / 3

    fun getOneThirdScreenWidth(context: Context): Int = getScreenWidthHeight(context).x / 3


    private fun getScreenWidthHeight(context: Context): Point {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val point = Point()
        display.getRealSize(point)
        return point
    }
}
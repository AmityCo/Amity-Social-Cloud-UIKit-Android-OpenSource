package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import com.amity.socialcloud.uikit.common.R

object AmityThemeUtil {

    fun setCurrentTheme(context: Context) {
        val sharedPref = context.getSharedPreferences("AMITY_PREF", Context.MODE_PRIVATE)
        val currentTheme = sharedPref.getInt("THEME", 1)
        if (currentTheme == 1) {
            context.setTheme(R.style.AmityAppTheme1)
        } else {
            context.setTheme(R.style.AmityAppTheme2)
        }
    }
}
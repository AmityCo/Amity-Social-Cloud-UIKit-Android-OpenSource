package com.amity.socialcloud.uikit.sample.localization

import android.content.Context
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import java.util.Locale

/**
 * Applies UIKit string overrides for the current device locale.
 *
 * Call this from Application.onCreate() after AmityUIKit4Manager.setup().
 *
 * Currently supported locales:
 *   - th (Thai)
 *
 * To force a specific locale regardless of device settings, pass it explicitly:
 *   AmityLocaleHelper.apply(context, "th")
 *
 * To let the device locale decide:
 *   AmityLocaleHelper.apply(context)
 */
object AmityLocaleHelper {

    fun apply(context: Context, forceLocale: String? = null) {
        val locale = forceLocale ?: Locale.getDefault().language
        when (locale) {
            "th" -> applyThai()
            // Add more locales here as needed, e.g.:
            // "ja" -> applyJapanese()
            // "ko" -> applyKorean()
        }
    }

    private fun applyThai() {
        DefaultAmityCommonStringProvider.setLocale("th", AmityCommonThaiStrings.strings)
        DefaultAmitySocialStringProvider.setLocale("th", AmitySocialThaiStrings.strings)
    }
}

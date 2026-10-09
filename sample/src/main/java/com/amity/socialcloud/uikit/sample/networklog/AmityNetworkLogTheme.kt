package com.amity.socialcloud.uikit.sample.networklog

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Type-chip and status palette.
 *
 * The UIKit palette provides one accent, one alert and a neutral ramp — it cannot express
 * eight mutually distinguishable categories. Six hues carry the activity family and
 * fill-vs-outline carries the request/state split, so eight values stay distinguishable
 * using six hues, which is what lets the set survive both themes.
 *
 * Design: front-end-tech-specs/assets/uikit-network-logs-design.html
 */
object AmityNetworkLogPalette {

    data class ChipColors(val fg: Color, val bg: Color, val outlined: Boolean)

    @Composable
    @ReadOnlyComposable
    fun chip(label: String): ChipColors {
        val dark = isSystemInDarkTheme()
        return when (label) {
            "API" -> if (dark) ChipColors(Color(0xFF7FA5FF), Color(0xFF1A2440), false)
            else ChipColors(Color(0xFF1054DE), Color(0xFFE7EEFC), false)
            "UPLD" -> if (dark) ChipColors(Color(0xFFA79CFB), Color(0xFF241F45), false)
            else ChipColors(Color(0xFF4C3FD6), Color(0xFFEAE8FB), false)
            "DOWN" -> if (dark) ChipColors(Color(0xFF4FC0C8), Color(0xFF10312F), false)
            else ChipColors(Color(0xFF00707A), Color(0xFFDDF0F1), false)
            "HLS" -> if (dark) ChipColors(Color(0xFFE0A45C), Color(0xFF35240F), false)
            else ChipColors(Color(0xFF8A5A00), Color(0xFFFAEFDC), false)
            "IMG" -> if (dark) ChipColors(Color(0xFFA5A9B5), Color(0xFF2B2E38), false)
            else ChipColors(Color(0xFF636878), Color(0xFFECEEF2), false)
            "MQTT" -> if (dark) ChipColors(Color(0xFFCF8CE8), Color(0xFF2C1938), false)
            else ChipColors(Color(0xFF8B2FB8), Color(0xFFF4E6FA), false)
            // outline = state-only, no request/response
            "CONN" -> if (dark) ChipColors(Color(0xFFCF8CE8), Color.Transparent, true)
            else ChipColors(Color(0xFF8B2FB8), Color.Transparent, true)
            else -> if (dark) ChipColors(Color(0xFFE0A45C), Color.Transparent, true)
            else ChipColors(Color(0xFF8A5A00), Color.Transparent, true)
        }
    }

    @Composable
    @ReadOnlyComposable
    fun status(statusClass: AmityNetworkLogStatusClass): Color {
        val dark = isSystemInDarkTheme()
        return when (statusClass) {
            AmityNetworkLogStatusClass.SUCCESS -> if (dark) Color(0xFF5FCB86) else Color(0xFF1F7A3D)
            AmityNetworkLogStatusClass.NEUTRAL -> if (dark) Color(0xFFA5A9B5) else Color(0xFF636878)
            AmityNetworkLogStatusClass.WARNING -> if (dark) Color(0xFFE0A45C) else Color(0xFF8A5A00)
            AmityNetworkLogStatusClass.ERROR -> if (dark) Color(0xFFFF7A62) else Color(0xFFFA4D30)
            AmityNetworkLogStatusClass.IN_FLIGHT -> Color(0xFF898E9E)
            AmityNetworkLogStatusClass.NONE -> if (dark) Color(0xFFA5A9B5) else Color(0xFF636878)
        }
    }
}

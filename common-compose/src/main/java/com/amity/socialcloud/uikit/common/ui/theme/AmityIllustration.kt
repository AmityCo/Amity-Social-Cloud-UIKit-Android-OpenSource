package com.amity.socialcloud.uikit.common.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

/**
 * Resolve a per-mode drawable against the UIKit's effective dark mode — the SAME
 * signal [AmityTheme.token] uses ([LocalAmityDarkMode], driven by
 * AmityUIKitConfigController.shouldUIKitInDarkTheme()).
 *
 * Prefer this over the Android night-qualified resource folder: the UIKit theme can be
 * force-set to DARK or LIGHT independently of the device, so a night-qualified resource
 * (which follows the device only) would desync from the design tokens. Reading
 * [LocalAmityDarkMode] keeps illustrations in lockstep with token colours in every case.
 */
@Composable
@DrawableRes
fun amityThemedDrawable(@DrawableRes light: Int, @DrawableRes dark: Int): Int =
    if (LocalAmityDarkMode.current) dark else light

/**
 * A brand illustration (or any decorative asset) that ships a per-mode light/dark variant.
 * The asset is NOT tinted — mode is handled by swapping the drawable, and the correct dark
 * signal is owned here so call sites cannot pick the wrong one.
 */
@Composable
fun AmityIllustration(
    @DrawableRes light: Int,
    @DrawableRes dark: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Image(
        imageVector = ImageVector.vectorResource(id = amityThemedDrawable(light, dark)),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

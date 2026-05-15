package com.amity.socialcloud.uikit.community.compose.localization

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeScope
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

/**
 * Resolves a user-facing string through the full priority chain:
 * 1. Config.json text (remote override)
 * 2. Programmatic override (setOverrides)
 * 3. Locale bundle (setLocale)
 * 4. strings.xml (Android resource default)
 * 5. Key name fallback
 *
 * @param fallbackKey The localization key to use when config.json has no text.
 */
@Composable
fun AmityComposeScope.amitySocialConfigString(fallbackKey: String): String {
    val configText = getConfig().getText()
    if (configText.isNotEmpty()) return configText
    return amitySocialString(fallbackKey)
}

package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityIllustration
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * AmityEmptyState — the centered, static placeholder shown when a list/screen has no content (or
 * one that failed to load): a visual slot (a decorative illustration or a tinted icon glyph), a
 * [title], an optional [description], and up to two optional CTA slots.
 *
 * A composite atom: it owns only its own Icon-tint/Title/Description token trio (see the private
 * token bindings below). The illustration is an opaque, un-tokenized light/dark asset pair — mode
 * is a plain drawable swap via [AmityIllustration], never a recolor — and both action slots render
 * nested [AmityButton] instances that own their own tokens. Presentational only: this atom carries
 * no interaction state of its own, only the [primaryAction]/[secondaryAction] callbacks.
 *
 * @param variant which visual slot renders — [AmityEmptyStateVariant.ILLUSTRATION] (a 160×160
 * decorative asset pair) or [AmityEmptyStateVariant.ICON] (a 64×64 tinted glyph).
 * @param title the headline text.
 * @param modifier applied to the outer column; pass a sizing modifier (e.g. `Modifier.fillMaxSize()`)
 * from the caller so the whole placeholder centers within its container.
 * @param illustrationLight the light-mode illustration asset; required when [variant] is
 * [AmityEmptyStateVariant.ILLUSTRATION], ignored otherwise.
 * @param illustrationDark the dark-mode counterpart of [illustrationLight]; ignored otherwise.
 * @param icon the glyph tinted via this atom's Icon token; required when [variant] is
 * [AmityEmptyStateVariant.ICON], ignored otherwise.
 * @param description optional supporting text under [title].
 * @param primaryAction the filled/primary CTA, rendered as an [AmityButton] `MAIN`/`FILLED`/`PRIMARY`/`LG`.
 * @param secondaryAction the ghost/text-link CTA, rendered as an [AmityButton] `MAIN`/`GHOST`/`PRIMARY`/`LG`.
 * When both are set, [primaryAction] renders above [secondaryAction].
 */
@Composable
fun AmityEmptyState(
    variant: AmityEmptyStateVariant,
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes illustrationLight: Int? = null,
    @DrawableRes illustrationDark: Int? = null,
    @DrawableRes icon: Int? = null,
    description: String? = null,
    primaryAction: AmityEmptyStateAction? = null,
    secondaryAction: AmityEmptyStateAction? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RootGap, Alignment.CenterVertically),
    ) {
        if (variant == AmityEmptyStateVariant.ILLUSTRATION && illustrationLight != null && illustrationDark != null) {
            AmityIllustration(
                light = illustrationLight,
                dark = illustrationDark,
                modifier = Modifier.size(IllustrationSize),
            )
        }

        // This wrapper is present regardless of variant — it simply omits its own icon glyph
        // child when the visual slot is an illustration instead.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContentHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(IconToContentGap),
        ) {
            if (variant == AmityEmptyStateVariant.ICON && icon != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    tint = AmityTheme.token(AmityColorToken.IconEmptyStateIconDefault),
                    modifier = Modifier.size(IconSize),
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(TextToActionsGap),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TitleFontSize,
                            lineHeight = TitleLineHeight,
                            letterSpacing = (-0.4).sp,
                            color = AmityTheme.token(AmityColorToken.TextEmptyStateTitleDefault),
                        ),
                    )
                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                letterSpacing = (-0.4).sp,
                                color = AmityTheme.token(AmityColorToken.TextEmptyStateDescriptionDefault),
                            ),
                        )
                    }
                }

                if (primaryAction != null || secondaryAction != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(ActionsGap),
                    ) {
                        primaryAction?.let { action ->
                            AmityButton(
                                variant = AmityButtonVariant.MAIN,
                                style = AmityButtonStyle.FILLED,
                                hierarchy = AmityButtonHierarchy.PRIMARY,
                                mainSize = AmityMainButtonSize.LG,
                                label = action.label,
                                icon = action.icon,
                                onClick = action.onPress,
                            )
                        }
                        secondaryAction?.let { action ->
                            AmityButton(
                                variant = AmityButtonVariant.MAIN,
                                style = AmityButtonStyle.GHOST,
                                hierarchy = AmityButtonHierarchy.PRIMARY,
                                mainSize = AmityMainButtonSize.LG,
                                label = action.label,
                                icon = action.icon,
                                onClick = action.onPress,
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Which visual slot [AmityEmptyState] renders. */
enum class AmityEmptyStateVariant {
    ILLUSTRATION,
    ICON,
}

/** One of [AmityEmptyState]'s up-to-two optional CTA slots. */
data class AmityEmptyStateAction(
    val label: String,
    @DrawableRes val icon: Int? = null,
    val onPress: () -> Unit,
)

// ---------------------------------------------------------------------------
// Geometry constants — explicit, not derived.
// ---------------------------------------------------------------------------

private val RootGap = 4.dp
private val IllustrationSize = 160.dp
private val IconSize = 64.dp
private val ContentHorizontalPadding = 24.dp
private val IconToContentGap = 8.dp
private val TextToActionsGap = 16.dp
private val ActionsGap = 16.dp

private val TitleFontSize = 17.sp
private val TitleLineHeight = 24.sp

package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Atomic user/community avatar.
 *
 * Renders one of three content types (Image photo, Icon default-person glyph, Text initials)
 * inside a presentational box that varies on Style (rounded circle / squared, radius scales with size),
 * Size (8 intrinsic sizes), and State (default / hover / skeleton shimmer). It can carry an
 * optional profile border ring, a bottom-right indicator badge slot, and a caption label.
 *
 * Every coloured part binds a semantic color token via [AmityTheme.token] — no hardcoded
 * avatar colours. Image-variant avatars carry no colour token (the photo is content).
 *
 * This atom is the token-conformant successor to the legacy AmityAvatarView family in
 * ui.elements; that view is left untouched. Consumers migrating to the design-token system
 * should adopt AmityAvatar.
 */

/** Which content the avatar renders. */
enum class AmityAvatarVariant { Image, Icon, Text }

/** Frame style — full circle, or squared corners whose radius scales with size. */
enum class AmityAvatarStyle { Rounded, Squared }

/** The 8 intrinsic avatar sizes, in dp. */
enum class AmityAvatarSize(val dp: Dp) {
    Size16(16.dp),
    Size24(24.dp),
    Size28(28.dp),
    Size32(32.dp),
    Size40(40.dp),
    Size56(56.dp),
    Size64(64.dp),
    Size120(120.dp),
}

/** Visual state. Skeleton replaces the surface with the shimmer treatment. */
enum class AmityAvatarState { Default, Hover, Skeleton }

@Composable
fun AmityAvatar(
    variant: AmityAvatarVariant,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    initials: String? = null,
    @DrawableRes icon: Int? = null,
    style: AmityAvatarStyle = AmityAvatarStyle.Rounded,
    size: AmityAvatarSize = AmityAvatarSize.Size40,
    state: AmityAvatarState = AmityAvatarState.Default,
    borderWidth: Int = 0,
    indicator: (@Composable () -> Unit)? = null,
    label: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val dimension = size.dp
    // Fallback-glyph dimension is per-size, from the Avatar atom spec (§ Geometry) — NOT a fixed
    // inset. A fixed inset undersizes the glyph at small sizes (e.g. a 32 avatar would leave only
    // 12 dp of glyph instead of the spec's 20) and oversizes it at large ones.
    val glyphDimension = iconGlyphSize(size, style)

    val shape: Shape = when (style) {
        AmityAvatarStyle.Rounded -> RoundedCornerShape(dimension / 2)
        AmityAvatarStyle.Squared -> RoundedCornerShape(squaredCornerRadius(size))
    }

    val fallbackSurface = AmityTheme.token("Surface/Avatar/Profile/Default")
    val glyphTint = AmityTheme.token("Icon/Avatar/Default")
    val initialsColor = AmityTheme.token("Text/Avatar/Atomic/General")
    val borderColor = AmityTheme.token("Border/Avatar/Profile/Default")
    val labelColor = AmityTheme.token("Text/Avatar/Label/Default")
    val skeletonSurface = AmityTheme.token("Surface/SkeletonEffect/Default")
    // 50% black scrim — the value the design's atomic Alpha/Black/500 carries; bound via the
    // resolvable media-overlay semantic (alias-path enum entries cannot resolve).
    val hoverScrim = AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack)

    var avatarModifier = Modifier.size(dimension)
    if (onClick != null) {
        avatarModifier = avatarModifier.clickable { onClick() }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = avatarModifier) {
            Box(
                modifier = Modifier
                    .size(dimension)
                    .let { m -> if (borderWidth > 0) m.border(borderWidth.dp, borderColor, shape) else m }
                    .let { m -> if (borderWidth > 0) m.padding((borderWidth * 0.5f).dp) else m }
                    .clip(shape),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    state == AmityAvatarState.Skeleton -> {
                        Box(
                            modifier = Modifier
                                .size(dimension)
                                .background(skeletonSurface),
                        )
                    }

                    variant == AmityAvatarVariant.Image -> {
                        AvatarImage(
                            imageUrl = imageUrl,
                            dimension = dimension,
                            glyphDimension = glyphDimension,
                            fallbackSurface = fallbackSurface,
                            glyphTint = glyphTint,
                            icon = icon,
                        )
                    }

                    variant == AmityAvatarVariant.Text -> {
                        Box(
                            modifier = Modifier
                                .size(dimension)
                                .background(fallbackSurface),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = initials?.trim()?.take(2)?.uppercase().orEmpty(),
                                color = initialsColor,
                                // Per-size initials type from the Avatar master (Type=Text variants):
                                // font size ≈ 0.5×avatar at weight 400 (Regular) — NOT 0.4×/SemiBold,
                                // which rendered the initials too small and too heavy.
                                style = AmityTheme.typography.titleBold.copy(
                                    fontSize = initialsFontSize(size),
                                    lineHeight = initialsLineHeight(size),
                                    fontWeight = FontWeight.Normal,
                                ),
                            )
                        }
                    }

                    else -> {
                        // Icon variant: default fallback glyph sized per the spec's per-size table.
                        Box(
                            modifier = Modifier
                                .size(dimension)
                                .background(fallbackSurface),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (icon != null) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = icon),
                                    contentDescription = "Avatar",
                                    tint = glyphTint,
                                    modifier = Modifier.size(glyphDimension),
                                )
                            }
                        }
                    }
                }

                // Hover state: an edit-photo affordance drawn over the base content — a full-cover
                // scrim plus a centered camera glyph, not merely a fill tint.
                if (state == AmityAvatarState.Hover) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(hoverScrim),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_camera_r),
                            contentDescription = null,
                            tint = glyphTint,
                            modifier = Modifier.size(glyphDimension),
                        )
                    }
                }
            }

            // Indicator badge slot, anchored bottom-right (Badge atom instance supplied by caller).
            if (indicator != null && state != AmityAvatarState.Skeleton) {
                Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                    indicator()
                }
            }
        }

        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                color = labelColor,
                style = AmityTheme.typography.caption,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

/** Initials font size per avatar size, from the Avatar master's Type=Text variants (weight 400). */
private fun initialsFontSize(size: AmityAvatarSize): TextUnit = when (size) {
    AmityAvatarSize.Size16 -> 10.sp
    AmityAvatarSize.Size24 -> 12.sp
    AmityAvatarSize.Size28 -> 12.sp
    AmityAvatarSize.Size32 -> 16.sp
    AmityAvatarSize.Size40 -> 20.sp
    AmityAvatarSize.Size56 -> 32.sp
    AmityAvatarSize.Size64 -> 32.sp
    AmityAvatarSize.Size120 -> 60.sp
}

/** Initials line height per avatar size, from the Avatar master's Type=Text variants. */
private fun initialsLineHeight(size: AmityAvatarSize): TextUnit = when (size) {
    AmityAvatarSize.Size16 -> 12.sp
    AmityAvatarSize.Size24 -> 16.sp
    AmityAvatarSize.Size28 -> 16.sp
    AmityAvatarSize.Size32 -> 20.sp
    AmityAvatarSize.Size40 -> 24.sp
    AmityAvatarSize.Size56 -> 40.sp
    AmityAvatarSize.Size64 -> 40.sp
    AmityAvatarSize.Size120 -> 72.sp
}

/** Fallback-glyph dimension per avatar size + style, from the Avatar atom spec (§ Geometry). */
private fun iconGlyphSize(size: AmityAvatarSize, style: AmityAvatarStyle): Dp = when (size) {
    AmityAvatarSize.Size16 -> 12.dp
    AmityAvatarSize.Size24 -> 16.dp
    AmityAvatarSize.Size28 -> 18.dp
    AmityAvatarSize.Size32 -> 20.dp
    // The 40 px master samples an asymmetric glyph: 24 Rounded / 28 Squared (spec § Geometry).
    AmityAvatarSize.Size40 -> if (style == AmityAvatarStyle.Squared) 28.dp else 24.dp
    AmityAvatarSize.Size56 -> 32.dp
    AmityAvatarSize.Size64 -> 32.dp
    AmityAvatarSize.Size120 -> 64.dp
}

/** Squared-style corner radius per avatar size — scales with size, never a fixed value. */
private fun squaredCornerRadius(size: AmityAvatarSize): Dp = when (size) {
    AmityAvatarSize.Size16 -> 4.dp
    AmityAvatarSize.Size24 -> 4.dp
    AmityAvatarSize.Size28 -> 4.dp
    AmityAvatarSize.Size32 -> 4.dp
    AmityAvatarSize.Size40 -> 8.dp
    AmityAvatarSize.Size56 -> 16.dp
    AmityAvatarSize.Size64 -> 16.dp
    AmityAvatarSize.Size120 -> 24.dp
}

@Composable
private fun AvatarImage(
    imageUrl: String?,
    dimension: Dp,
    glyphDimension: Dp,
    fallbackSurface: androidx.compose.ui.graphics.Color,
    glyphTint: androidx.compose.ui.graphics.Color,
    @DrawableRes icon: Int?,
) {
    if (imageUrl.isNullOrEmpty()) {
        ImageFallbackSurface(dimension, glyphDimension, fallbackSurface, glyphTint, icon)
        return
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Image(
        painter = painter,
        contentScale = ContentScale.Crop,
        contentDescription = "Avatar",
        modifier = Modifier.size(dimension),
    )
    if (painterState !is AsyncImagePainter.State.Success) {
        ImageFallbackSurface(dimension, glyphDimension, fallbackSurface, glyphTint, icon)
    }
}

@Composable
private fun ImageFallbackSurface(
    dimension: Dp,
    glyphDimension: Dp,
    fallbackSurface: androidx.compose.ui.graphics.Color,
    glyphTint: androidx.compose.ui.graphics.Color,
    @DrawableRes icon: Int?,
) {
    Box(
        modifier = Modifier
            .size(dimension)
            .background(fallbackSurface),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = glyphTint,
                modifier = Modifier.size(glyphDimension),
            )
        }
    }
}

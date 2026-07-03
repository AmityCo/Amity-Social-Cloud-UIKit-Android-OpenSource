package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// Disabled-state colour for any AmityTheme/brand colour. Returns the given colour
// with the alpha that matches the current theme — 0.8f in light mode, 0.3f in dark.
// Use anywhere a Button is disabled (Text color, Icon tint, disabledContainerColor,
// disabledContentColor). Composable because it reads the live theme.
@Composable
@ReadOnlyComposable
fun amityDisabledColor(color: Color): Color =
    color.copy(alpha = if (isUIKitInDarkTheme()) 0.3f else 1f)

// Brand and feature-specific colors that don't map to AmityTheme.colors.* tokens
// (config.json palette). Keep them centralised so a rename here propagates everywhere,
// and so the "left intentionally hardcoded" hex literals become greppable by name.

// Event host badge (post, comment, event card)
val amityEventHostBadgeBackground = Color(0xFFEAE2FF)
val amityEventHostBadgeContent = Color(0xFF4B1BD0)

// Live indicator red — appears across clip create, livestream chat/player, story live targets
val amityLiveBadgeRed = Color(0xFFFF305A)
val amityLiveBadgeRedAlt = Color(0xFFFF2D55)
val amityLiveBadgeRedAccent = Color(0xFFFF3B5C)
val amityLiveBadgeGradientEnd = Color(0xFFFF0000)

// Camera shutter (story + clip create)
val amityCameraShutterIdleRing = Color(0xFF606170)
val amityCameraShutterRecordFill = Color(0xFFF72C40)

// Story ring gradient
val amityStoryRingGradientStart = Color(0xFF339AF9)
val amityStoryRingGradientEnd = Color(0xFF78FA58)

// Livestream dark surfaces that fall outside the dark-mode config.json palette
val amityLivestreamSurfaceElevated = Color(0xFF1C1C1E)
val amityLivestreamSurfaceDivider = Color(0xFF48484A)
val amityLivestreamBorder = Color(0xFF38383A)
val amityCoHostWaitingBackground = Color(0xFF4A5568)

// Media canvas — opaque black backdrop for video/image/clip/story viewers,
// independent of light/dark theme (media is always presented on black).
val amityMediaSurface = Color.Black

// Foreground content (text/icon) placed on top of amityMediaSurface — always white.
val amityMediaContentColor = Color.White

// Shadow tint used for elevated card spot/ambient shadow in livestream player.
val amityLivestreamShadowTint = Color(0xFF28293D)

// Generic white/black aliases — use these instead of Color.White/Color.Black
// in component code so all colour references go through this file.
val amityColorWhite = Color.White
val amityColorBlack = Color.Black
val amityColorGray = Color.Gray

// Event avatar placeholder background — beige/tan tone used when an event's avatar fails to load.
val amityEventAvatarPlaceholderBackground = Color(0xFFDBCABD)

// Base colour for the reaction-name tooltip in AmityReactionPicker — neutral dark grey,
// rendered with alpha at the call site (~0.67f).
val amityReactionTooltipBase = Color(0xFF333333)

// iOS-style red used for the chat message-failed indicator; visually distinct from
// AmityTheme.colors.alert (#FA4D30) and intentionally kept as a separate brand colour.
val amityChatErrorRed = Color(0xFFFF3B30)

val amityStoryEngagementBackground = Color(0xFF292B32)
val amityStoryEngagementIcon = Color(0xFFA5A9B5)

val amityCreateStorySelectionSelectedBackground = amityColorSecondaryShade1
val amityCreateStorySelectionUnselectedBackground = amityColorSecondary
val amityCreateStorySelectionSelectedText = amityColorSecondaryShade1
val amityCreateStorySelectionUnselectedText = amityColorSecondaryShade2

val amityLivestreamChatBubbleBackground = amityColorBaseShade1

@Composable
fun amityAvatarPlaceholderBackground() =
    if (isUIKitInDarkTheme()) amityColorPrimaryShade1
    else amityColorPrimaryShade2

@Composable
fun amityDeletedAvatarPlaceholderBackground() =
    if (isUIKitInDarkTheme()) amityColorSecondaryShade1
    else amityColorSecondaryShade2

@Composable
fun amityNotificationTrayHighlightBackground() =
    if (isUIKitInDarkTheme()) amityColorPrimary
    else amityColorPrimaryShade3

@Composable
fun amityProductTagBadgeCountBackground() =
    if (isUIKitInDarkTheme()) amityColorBaseShade4
    else amityColorBase
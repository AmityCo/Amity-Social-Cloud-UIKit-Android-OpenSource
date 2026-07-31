package com.amity.socialcloud.uikit.chat.compose.common

/**
 * Chat's avatar-initials rule, in one place: exactly **one letter** — the display name's first
 * character, uppercased — or null when there is no usable name (callers switch the Avatar atom
 * to its Icon fallback on null/empty).
 *
 * The Avatar atom renders whatever `initials` string it is given (clamped to max 2 chars), so
 * always derive through this helper — never pass a raw display name.
 */
fun String?.toChatAvatarInitial(): String? =
    this?.trim()?.firstOrNull()?.uppercase()

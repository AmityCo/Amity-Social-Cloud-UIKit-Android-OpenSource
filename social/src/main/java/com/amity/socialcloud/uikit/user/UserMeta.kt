package com.amity.socialcloud.uikit.user

import android.content.Context
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.user.UserType.*
import com.google.gson.JsonObject

typealias UserMeta = JsonObject

private const val KEY_USER_TYPE = "userType"

enum class UserType(val key: String) {
  Client("user"),
  Coach("coach"),
  Writer("writer"),
  Team("team"),
}

fun AmityUser.userMeta(): UserMeta? = getMetadata()

fun UserMeta.userType(): UserType? {
  val userTypeString = get(KEY_USER_TYPE)?.asString
  return UserType.values().firstOrNull { it.key == userTypeString }
}

fun UserType.labelSuffix(context: Context): String? =
  when (this) {
    Client -> null
    Coach  -> context.getString(R.string.amity_username_suffix_coach)
    Writer -> context.getString(R.string.amity_username_suffix_writer)
    Team   -> context.getString(R.string.amity_username_suffix_team)
  }

fun UserType.labelTextColor(context: Context): Int =
  when (this) {
    Client              -> ContextCompat.getColor(context, R.color.post_header_username_regular)
    Coach, Writer, Team -> ContextCompat.getColor(context, R.color.post_header_username_highlighted)
  }

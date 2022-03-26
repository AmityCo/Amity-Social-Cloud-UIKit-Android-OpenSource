package com.amity.socialcloud.uikit.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmityMenuItem(
    val id: Int,
    val title: String,
    var isAlertItem: Boolean = false
) : Parcelable
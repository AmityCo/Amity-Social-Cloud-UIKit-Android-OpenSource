package com.amity.socialcloud.uikit.common.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AmityMenuItem(
    val id: Int,
    val title: String,
    var isAlertItem: Boolean = false
) : Parcelable
package com.amity.socialcloud.uikit.sample.env

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Environment(
    var apiKey: String,
    var httpUrl: String,
    var socketUrl: String
) : Parcelable
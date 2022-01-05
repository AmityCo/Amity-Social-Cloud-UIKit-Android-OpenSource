package com.amity.socialcloud.uikit.community.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AmitySelectCategoryItem(
    var categoryId: String = "",
    var name: String = ""
) : Parcelable
package com.amity.socialcloud.uikit.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AmitySelectMemberItem(
    var id: String = "",
    var avatarUrl: String = "",
    var name: String = "",
    var subTitle: String = "",
    var isSelected: Boolean = false
) : Parcelable
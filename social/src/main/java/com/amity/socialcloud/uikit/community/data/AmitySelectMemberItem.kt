package com.amity.socialcloud.uikit.community.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Model class to handle Select Members
 * @author sumitlakra
 * @date 06/17/2020
 */
@Parcelize
data class AmitySelectMemberItem(
    var id: String = "",
    var avatarUrl: String = "",
    var name: String = "",
    var subTitle: String = "",
    var isSelected: Boolean = false
) : Parcelable
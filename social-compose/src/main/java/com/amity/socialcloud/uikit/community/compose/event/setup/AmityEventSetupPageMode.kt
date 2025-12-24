package com.amity.socialcloud.uikit.community.compose.event.setup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityEventSetupPageMode : Parcelable {
    data class Create(val communityId: String? = null) : AmityEventSetupPageMode()
    data class Edit(val eventId: String) : AmityEventSetupPageMode()
}

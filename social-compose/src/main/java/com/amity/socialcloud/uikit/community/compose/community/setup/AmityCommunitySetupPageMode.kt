package com.amity.socialcloud.uikit.community.compose.community.setup

import android.os.Parcelable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityCommunitySetupPageMode : Parcelable {
    object Create : AmityCommunitySetupPageMode()

    data class Edit(val community: AmityCommunity) : AmityCommunitySetupPageMode()
}
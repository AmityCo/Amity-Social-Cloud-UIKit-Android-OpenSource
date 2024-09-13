package com.amity.socialcloud.uikit.common.utils

import android.content.Intent
import android.os.Build
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

fun Intent.getCommunity(key: String): AmityCommunity? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        extras?.getParcelable(key, AmityCommunity::class.java)
    } else {
        extras?.getParcelable(key)
    }
}

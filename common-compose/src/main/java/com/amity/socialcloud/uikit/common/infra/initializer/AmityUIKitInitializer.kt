package com.amity.socialcloud.uikit.common.infra.initializer

import android.content.Context
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB


object AmityUIKitInitializer {

    @Synchronized
    fun init(context: Context) {
        AmityAppContext.init(context)
    }
}
package com.amity.socialcloud.uikit.common.infra.provider

import com.amity.socialcloud.sdk.infra.android.BaseInitProvider
import com.amity.socialcloud.uikit.common.infra.initializer.AmityUIKitInitializer
import com.ekoapp.ekosdk.internal.init.AmityCoreSDKInitializer
import java.util.concurrent.atomic.AtomicBoolean


class AmityUIKitContentProvider : BaseInitProvider() {
    override fun onCreate(): Boolean {
        AmityUIKitInitializer.init(context!!)
        isInitialized.set(true)
        return true
    }

    companion object {
        private val isInitialized = AtomicBoolean(false)
        fun isInitialized(): Boolean {
            return isInitialized.get()
        }
    }
}
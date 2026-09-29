@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.sample

import android.app.Application
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.model.core.file.AmityFileAccessType
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import com.amity.socialcloud.uikit.sample.localization.AmityLocaleHelper
import com.amity.socialcloud.uikit.sample.behavior.SampleGlobalBehavior

class AmitySampleApp : Application()  {

    override fun onCreate() {
        super.onCreate()
        APP = this

        // ponytail: temporary — records what the SDK actually sends, so a module
        // switched off can be checked against the wire. Started before setup
        // because the SDK connects during it, and anything sent in that window
        // would otherwise be invisible. Remove with ApiTrace.kt.
        ApiTrace.start(this)

        // V4 setup
        AmityUIKit4Manager.setup(
            apiKey = SamplePreferences.getApiKey().get(),
            endpoint =  AmityEndpoint.CUSTOM(
                SamplePreferences.getHttpUrl().get(),
                SamplePreferences.getMqttBroker().get(),
                SamplePreferences.getUploadUrl().get(),
            )
        )

        // Sample behaviour overrides. Registered once here rather than at login, because a
        // product tap can happen in any session and the override reads its own toggle each time.
        AmityUIKit4Manager.behavior.globalBehavior = SampleGlobalBehavior()

        // Apply locale-specific string overrides (Thai demo)
        AmityLocaleHelper.apply(this)

        // OPTIONAL: Set the default file access type for uploaded files
        AmityCoreClient.setUploadedFileAccessType(AmityFileAccessType.PUBLIC)

    }

    companion object {
        lateinit var APP: AmitySampleApp
    }


}
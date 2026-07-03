package com.amity.socialcloud.uikit.sample

import android.app.Application
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.model.core.file.AmityFileAccessType
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import com.amity.socialcloud.uikit.sample.localization.AmityLocaleHelper

class AmitySampleApp : Application()  {

    override fun onCreate() {
        super.onCreate()
        APP = this

        // V4 setup
        AmityUIKit4Manager.setup(
            apiKey = SamplePreferences.getApiKey().get(),
            endpoint =  AmityEndpoint.CUSTOM(
                SamplePreferences.getHttpUrl().get(),
                SamplePreferences.getMqttBroker().get(),
                SamplePreferences.getUploadUrl().get(),
            )
        )

        // Apply locale-specific string overrides (Thai demo)
        AmityLocaleHelper.apply(this)

        // OPTIONAL: Set the default file access type for uploaded files
        AmityCoreClient.setUploadedFileAccessType(AmityFileAccessType.PUBLIC)

    }

    companion object {
        lateinit var APP: AmitySampleApp
    }


}
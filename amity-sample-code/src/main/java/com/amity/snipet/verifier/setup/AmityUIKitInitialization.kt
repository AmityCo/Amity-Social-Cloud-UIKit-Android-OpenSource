package com.amity.snipet.verifier.setup

import android.app.Application
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.uikit.AmityUIKit4Manager

class AmityUIKitInitialization {

    /* begin_sample_code
     gist_id: 63fb4a2fcb01ec2a4139d9041a9663f6
     filename: AmityUIKitInitialization.kt
     asc_page: https://docs.amity.co/chat/android/getting-started#initialization
     description: Setup with EU endpoint
     */
    class MyApp : Application() {
        override fun onCreate() {
            super.onCreate()
            // setup() function should be called once per application lifecycle
            AmityUIKit4Manager.setup(
                apiKey = "YOUR_API_KEY",
                endpoint =  AmityEndpoint.EU
            )
        }
    }
    /* end_sample_code */

}
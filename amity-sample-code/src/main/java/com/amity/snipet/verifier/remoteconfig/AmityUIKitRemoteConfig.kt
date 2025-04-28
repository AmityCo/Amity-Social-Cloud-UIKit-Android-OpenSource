package com.amity.snipet.verifier.remoteconfig

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import io.reactivex.rxjava3.schedulers.Schedulers


class AmityUIKitRemoteConfig {
    /* begin_sample_code
     gist_id: dafb85d012ddca5ce0c421c2580b3501
     filename: AmityUIKitRemoteConfig.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4
     description: sync network config
     */
    fun syncNetworkConfig() {
        AmityUIKit4Manager.syncNetworkConfig()
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                // Success
            }
            .doOnError {
                // Exception
            }
            .subscribe()

    }
    /* end_sample_code */
}
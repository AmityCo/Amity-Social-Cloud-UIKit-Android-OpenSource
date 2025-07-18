package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint

object SampleUploadUrl {

    fun get(): String {
        return get(SampleEnv.STAGING)
    }

    fun get(environment: String): String {
        when (environment) {
            SampleEnv.LOCAL -> {
                return "http://172.16.224.113:3000/"
            }
            SampleEnv.DEV -> {
                return "https://upload.dev.amity.co/"
            }
            SampleEnv.STAGING -> {
                return "https://upload.staging.amity.co/"
            }
            SampleEnv.PRODUCTION_SG -> {
                return AmityEndpoint.SG.uploadEndpoint
            }
            else -> {
                return environment
            }
        }
    }
}
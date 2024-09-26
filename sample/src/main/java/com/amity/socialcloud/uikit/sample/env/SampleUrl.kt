package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint

object SampleUrl {

    fun get(): String {
        return get(SampleEnv.STAGING)
    }

    fun get(environment: String): String {
        when (environment) {
            SampleEnv.LOCAL -> {
                return "http://172.16.224.113:3000/"
            }
            SampleEnv.DEV -> {
                return "https://api.dev.amity.co/"
            }
            SampleEnv.STAGING -> {
                return "https://api.staging.amity.co/"
            }
            SampleEnv.PRODUCTION_SG -> {
                return AmityEndpoint.SG.httpEndpoint
            }
            else -> {
                return environment
            }
        }
    }
}
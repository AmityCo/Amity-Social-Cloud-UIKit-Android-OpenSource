package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint


object SampleBroker {

    fun get(): String {
        return get(SampleEnv.STAGING)
    }

    fun get(environment: String): String {
        when (environment) {
            SampleEnv.LOCAL -> {
                return "ssq.local.amity.co"
            }
            SampleEnv.DEV -> {
                return "ssq.dev.amity.co"
            }
            SampleEnv.STAGING -> {
                return "ssq.staging.amity.co"
            }
            SampleEnv.PRODUCTION_SG -> {
                return AmityEndpoint.SG.mqttEndpoint
            }
            SampleEnv.PRODUCTION_EU -> {
                return AmityEndpoint.EU.mqttEndpoint
            }
            SampleEnv.PRODUCTION_US -> {
                return AmityEndpoint.US.mqttEndpoint
            }
            else -> {
                return environment
            }
        }
    }

}
package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint


object SampleBroker {

    fun get(): String {
        return get(SampleEnv.PRODUCTION_SG)
    }

    fun get(environment: String): String {
        return AmityEndpoint.SG.mqttEndpoint
    }

}
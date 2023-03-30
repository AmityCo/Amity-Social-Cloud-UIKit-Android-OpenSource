package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.uikit.sample.env.SampleEnv.DEV
import com.amity.socialcloud.uikit.sample.env.SampleEnv.LOCAL
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_EU
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_SG
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_US
import com.amity.socialcloud.uikit.sample.env.SampleEnv.STAGING


object SampleAPIKey {

    fun get() : String {
        return get(PRODUCTION_SG)
    }

    fun get(environment: String): String {
        return "b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78"
    }

}
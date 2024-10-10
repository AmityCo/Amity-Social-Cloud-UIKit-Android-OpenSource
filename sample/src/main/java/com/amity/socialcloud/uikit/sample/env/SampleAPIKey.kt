package com.amity.socialcloud.uikit.sample.env

import com.amity.socialcloud.uikit.sample.env.SampleEnv.DEV
import com.amity.socialcloud.uikit.sample.env.SampleEnv.LOCAL
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_EU
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_SG
import com.amity.socialcloud.uikit.sample.env.SampleEnv.PRODUCTION_US
import com.amity.socialcloud.uikit.sample.env.SampleEnv.STAGING


object SampleAPIKey {

    fun get() : String {
        return get(STAGING)
    }

    fun get(environment: String): String {
        when (environment) {
            LOCAL -> {
                return "b3bae0593bd9a5671d3e8a1d035b1688d100dab3b9356e79"
            }
            DEV -> {
                return "b0ecee0c39dca1651d628b1c535d15dbd30ad9b0eb3c3a2f"
            }
            STAGING -> {
                return "b0efe90c3bdda2304d628918520c1688845889e4bc363d2c"
            }
            PRODUCTION_SG -> {
                return "b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78"
            }
            PRODUCTION_US -> {
                return "b0ecbd0f33d2a4344937d84a530a118e85088be5e83d6c2a"
            }
            PRODUCTION_EU -> {
                return "b0ecbd0f33d2f8371e63de14070a1f8ed009ddeabf3d687e"
            }
            else -> {
                return ""
            }
        }
    }

}
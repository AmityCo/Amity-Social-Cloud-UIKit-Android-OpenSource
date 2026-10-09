package com.amity.socialcloud.uikit.common.config

import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * PDT-5753. `AmityUIKitConfigController.getChannelListSortBy` carries the fallback itself rather
 * than relying on the Kotlin default on `channelListSortBy`, on the grounds that Gson does not
 * apply Kotlin default values to a key the JSON omits. This pins that reason, so the fallback is
 * not removed later as redundant.
 */
class ChatFeatureFlagsGsonTest {

    private val gson = GsonBuilder().create()

    @Test
    fun `a missing channel_list_sort_by should deserialize to null, not the Kotlin default`() {
        val flags = gson.fromJson(
            """{ "enabled_channel_types": ["conversation"] }""",
            AmityUIKitConfig.ChatFeatureFlags::class.java,
        )

        assertNull(flags.channelListSortBy)
    }

    @Test
    fun `a present channel_list_sort_by should deserialize to its value`() {
        val flags = gson.fromJson(
            """{ "channel_list_sort_by": "lastMessage" }""",
            AmityUIKitConfig.ChatFeatureFlags::class.java,
        )

        assertEquals("lastMessage", flags.channelListSortBy)
    }
}

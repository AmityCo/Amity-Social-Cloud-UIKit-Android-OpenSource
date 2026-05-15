package com.amity.socialcloud.uikit.common.localization

import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultAmityCommonStringProviderTest {

    @Test
    fun getString_returnsOverrideWhenSet() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_common_delete" to "Remove"))
        assertEquals("Remove", provider.getString("amity_common_delete"))
    }

    @Test
    fun getString_returnsLocaleOverrideWhenSet() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_common_delete" to "削除"))
        assertEquals("削除", provider.getString("amity_common_delete"))
    }

    @Test
    fun getString_overrideTakesPriorityOverLocale() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_common_delete" to "削除"))
        provider.setOverrides(mapOf("amity_common_delete" to "Remove"))
        assertEquals("Remove", provider.getString("amity_common_delete"))
    }

    @Test
    fun getString_returnsKeyWhenNoOverrideOrResource() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        assertEquals("amity_common_unknown_key", provider.getString("amity_common_unknown_key"))
    }

    @Test
    fun getString_formatsArgsCorrectly() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_common_ad_advertiser_name" to "Advertiser name: %s"))
        assertEquals("Advertiser name: Acme", provider.getString("amity_common_ad_advertiser_name", "Acme"))
    }

    @Test
    fun clearOverrides_removesAllOverrides() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_common_delete" to "Remove"))
        provider.clearOverrides()
        assertEquals("amity_common_delete", provider.getString("amity_common_delete"))
    }

    @Test
    fun clearLocale_deactivatesLocale() {
        val provider = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_common_delete" to "削除"))
        provider.clearLocale()
        assertEquals("amity_common_delete", provider.getString("amity_common_delete"))
    }
}

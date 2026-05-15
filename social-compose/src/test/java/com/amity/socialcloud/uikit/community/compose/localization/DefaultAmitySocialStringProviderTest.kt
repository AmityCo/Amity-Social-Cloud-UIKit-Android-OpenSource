package com.amity.socialcloud.uikit.community.compose.localization

import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultAmitySocialStringProviderTest {

    @Test
    fun getString_returnsOverrideWhenSet() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_link_copied" to "Copied!"))
        assertEquals("Copied!", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun getString_returnsLocaleOverrideWhenSet() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun getString_overrideTakesPriorityOverLocale() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_link_copied" to "Copied!"))
        assertEquals("Copied!", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun getString_returnsKeyWhenNoOverrideOrResource() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        assertEquals("amity_social_unknown_key", provider.getString("amity_social_unknown_key"))
    }

    @Test
    fun getString_formatsArgsCorrectly() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_follow_request_accepted" to "%s is now following you."))
        assertEquals("John is now following you.", provider.getString("amity_social_follow_request_accepted", "John"))
    }

    @Test
    fun clearOverrides_removesAllOverrides() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_link_copied" to "Copied!"))
        provider.clearOverrides()
        assertEquals("amity_social_link_copied", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun clearLocale_deactivatesLocale() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.clearLocale()
        assertEquals("amity_social_link_copied", provider.getString("amity_social_link_copied"))
    }

    // --- Priority chain tests ---

    @Test
    fun overrideAndLocale_overrideWins() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_link_copied" to "Override value"))
        assertEquals("Override value", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun localeWinsOverKeyFallback() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("th", mapOf("amity_social_link_copied" to "คัดลอกแล้ว"))
        assertEquals("คัดลอกแล้ว", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun keyFallbackWhenNothingSet() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        assertEquals("amity_social_some_key", provider.getString("amity_social_some_key"))
    }

    @Test
    fun overrideRemoval_fallsThroughToLocale() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_link_copied" to "Override value"))
        assertEquals("Override value", provider.getString("amity_social_link_copied"))

        provider.clearOverrides()
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun localeRemoval_fallsThroughToKeyFallback() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))

        provider.clearLocale()
        assertEquals("amity_social_link_copied", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun localeSwitching_respectsActiveLocale() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))

        provider.setLocale("th", mapOf("amity_social_link_copied" to "คัดลอกแล้ว"))
        assertEquals("คัดลอกแล้ว", provider.getString("amity_social_link_copied"))

        // Switch back to Japanese
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun partialOverride_onlyAffectsOverriddenKeys() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf(
            "amity_social_link_copied" to "コピーしました",
            "amity_social_cancel" to "キャンセル"
        ))
        provider.setOverrides(mapOf("amity_social_link_copied" to "Override value"))

        assertEquals("Override value", provider.getString("amity_social_link_copied"))
        assertEquals("キャンセル", provider.getString("amity_social_cancel"))
    }

    @Test
    fun emptyOverrideValue_isStillUsed() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_link_copied" to ""))

        assertEquals("", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun keyInLocaleButNotOverrides_returnsLocaleValue() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_cancel" to "Cancel override"))

        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun keyInNeitherOverridesNorLocale_returnsKeyFallback() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_link_copied" to "コピーしました"))
        provider.setOverrides(mapOf("amity_social_cancel" to "Cancel override"))

        assertEquals("amity_social_missing_key", provider.getString("amity_social_missing_key"))
    }

    @Test
    fun formatArgs_workWithOverride() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_greeting" to "Hello, %s! You have %d messages."))
        assertEquals(
            "Hello, Alice! You have 5 messages.",
            provider.getString("amity_social_greeting", "Alice", 5)
        )
    }

    @Test
    fun formatArgs_workWithLocale() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_social_greeting" to "%sさん、%d件のメッセージがあります。"))
        assertEquals(
            "太郎さん、3件のメッセージがあります。",
            provider.getString("amity_social_greeting", "太郎", 3)
        )
    }

    @Test
    fun formatArgs_workWithKeyFallback() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        // Key fallback returns the key name itself; format args are applied to it
        // The key has no format specifiers, so String.format returns it unchanged
        assertEquals(
            "amity_social_greeting",
            provider.getString("amity_social_greeting", "Alice")
        )
    }

    @Test
    fun multipleSetOverrides_merges() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_link_copied" to "Copied!"))
        provider.setOverrides(mapOf("amity_social_cancel" to "Cancel"))

        assertEquals("Copied!", provider.getString("amity_social_link_copied"))
        assertEquals("Cancel", provider.getString("amity_social_cancel"))
    }

    @Test
    fun multipleSetOverrides_laterValueOverwritesEarlier() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_social_link_copied" to "First"))
        provider.setOverrides(mapOf("amity_social_link_copied" to "Second"))

        assertEquals("Second", provider.getString("amity_social_link_copied"))
    }

    @Test
    fun setLocale_replacesEntireBundle() {
        val provider = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf(
            "amity_social_link_copied" to "コピーしました",
            "amity_social_cancel" to "キャンセル"
        ))
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
        assertEquals("キャンセル", provider.getString("amity_social_cancel"))

        // Second call replaces the entire bundle for "ja"
        provider.setLocale("ja", mapOf(
            "amity_social_link_copied" to "コピー済み"
        ))
        assertEquals("コピー済み", provider.getString("amity_social_link_copied"))
        // "amity_social_cancel" is no longer in the bundle — falls through to key fallback
        assertEquals("amity_social_cancel", provider.getString("amity_social_cancel"))
    }
}

package com.amity.socialcloud.uikit.community.compose.localization

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Integration tests for AmitySocialStringProvider with a full Thai locale bundle.
 *
 * Validates that the resolution chain works end-to-end with Thai translations,
 * including format arguments, priority rules, and locale lifecycle.
 */
class AmitySocialStringProviderThaiTest {

    private fun createProvider() = DefaultAmitySocialStringProvider(stringKeyToResId = emptyMap())

    private val thaiBundle = mapOf(
        "amity_cancel" to "ยกเลิก",
        "amity_delete" to "ลบ",
        "amity_comments" to "ความคิดเห็น",
        "amity_delete_comment" to "ลบความคิดเห็น",
        "amity_edit_comment" to "แก้ไขความคิดเห็น",
        "amity_report_comment" to "รายงานความคิดเห็น",
        "amity_delete_post_title" to "ลบโพสต์",
        "amity_discard_post_title" to "ยกเลิกโพสต์",
        "amity_discard_post_message" to "คุณต้องการยกเลิกโพสต์นี้หรือไม่?",
        "amity_leave_community" to "ออกจากชุมชน",
        "amity_close_community" to "ปิดชุมชน",
        "amity_post_to" to "โพสต์ไปยัง",
        "amity_social_link_copied" to "คัดลอกลิงก์แล้ว",
        "amity_social_follow_request_accepted" to "%s กำลังติดตามคุณอยู่",
        "amity_social_greeting" to "สวัสดี %s! คุณมี %d ข้อความ",
    )

    // --- Thai locale bundle tests ---

    @Test
    fun thaiLocale_resolvesAllKeys() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)

        assertEquals("ยกเลิก", provider.getString("amity_cancel"))
        assertEquals("ลบ", provider.getString("amity_delete"))
        assertEquals("ความคิดเห็น", provider.getString("amity_comments"))
        assertEquals("ลบความคิดเห็น", provider.getString("amity_delete_comment"))
        assertEquals("แก้ไขความคิดเห็น", provider.getString("amity_edit_comment"))
        assertEquals("รายงานความคิดเห็น", provider.getString("amity_report_comment"))
    }

    @Test
    fun thaiLocale_formatStringWithSingleArg() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)

        assertEquals(
            "สมชาย กำลังติดตามคุณอยู่",
            provider.getString("amity_social_follow_request_accepted", "สมชาย")
        )
    }

    @Test
    fun thaiLocale_formatStringWithMultipleArgs() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)

        assertEquals(
            "สวัสดี สมหญิง! คุณมี 7 ข้อความ",
            provider.getString("amity_social_greeting", "สมหญิง", 7)
        )
    }

    @Test
    fun thaiLocale_keyNotInBundle_fallsToKeyName() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)

        assertEquals("amity_social_unknown_key", provider.getString("amity_social_unknown_key"))
    }

    // --- Override takes priority over Thai locale ---

    @Test
    fun overrideTakesPriorityOverThaiLocale() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        provider.setOverrides(mapOf("amity_cancel" to "Abort"))

        assertEquals("Abort", provider.getString("amity_cancel"))
        // Non-overridden key still resolves from Thai locale
        assertEquals("ลบ", provider.getString("amity_delete"))
    }

    @Test
    fun clearOverrides_fallsBackToThaiLocale() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        provider.setOverrides(mapOf("amity_cancel" to "Abort"))
        assertEquals("Abort", provider.getString("amity_cancel"))

        provider.clearOverrides()
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))
    }

    // --- Thai locale lifecycle ---

    @Test
    fun clearThaiLocale_fallsToKeyFallback() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))

        provider.clearLocale()
        assertEquals("amity_cancel", provider.getString("amity_cancel"))
    }

    @Test
    fun switchFromThaiToJapanese() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))

        val japaneseBundle = mapOf(
            "amity_cancel" to "キャンセル",
            "amity_delete" to "削除",
        )
        provider.setLocale("ja", japaneseBundle)
        assertEquals("キャンセル", provider.getString("amity_cancel"))
        assertEquals("削除", provider.getString("amity_delete"))
    }

    @Test
    fun switchBetweenThaiAndJapanese_preservesBundles() {
        val provider = createProvider()

        val japaneseBundle = mapOf("amity_social_link_copied" to "コピーしました")
        provider.setLocale("ja", japaneseBundle)
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))

        provider.setLocale("th", thaiBundle)
        assertEquals("คัดลอกลิงก์แล้ว", provider.getString("amity_social_link_copied"))

        // Switch back to Japanese
        provider.setLocale("ja", japaneseBundle)
        assertEquals("コピーしました", provider.getString("amity_social_link_copied"))
    }

    // --- Full priority chain with Thai ---

    @Test
    fun fullPriorityChain_overrideWinsOverThaiLocaleWinsOverFallback() {
        val provider = createProvider()

        // Step 1: No locale, no override → key fallback
        assertEquals("amity_cancel", provider.getString("amity_cancel"))

        // Step 2: Thai locale set → Thai value
        provider.setLocale("th", thaiBundle)
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))

        // Step 3: Override set → override value
        provider.setOverrides(mapOf("amity_cancel" to "Custom Cancel"))
        assertEquals("Custom Cancel", provider.getString("amity_cancel"))

        // Step 4: Clear override → falls back to Thai locale
        provider.clearOverrides()
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))

        // Step 5: Clear locale → falls back to key name
        provider.clearLocale()
        assertEquals("amity_cancel", provider.getString("amity_cancel"))
    }

    @Test
    fun thaiLocale_emptyStringOverrideStillWins() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        provider.setOverrides(mapOf("amity_cancel" to ""))

        assertEquals("", provider.getString("amity_cancel"))
    }

    @Test
    fun thaiLocale_setLocaleReplacesEntireBundle() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        assertEquals("ยกเลิก", provider.getString("amity_cancel"))
        assertEquals("ลบ", provider.getString("amity_delete"))

        // Replace with smaller bundle — only amity_cancel
        provider.setLocale("th", mapOf("amity_cancel" to "ยกเลิกใหม่"))
        assertEquals("ยกเลิกใหม่", provider.getString("amity_cancel"))
        // amity_delete no longer in the bundle — falls to key fallback
        assertEquals("amity_delete", provider.getString("amity_delete"))
    }

    @Test
    fun thaiLocale_formatArgsWithOverrideTakesPriority() {
        val provider = createProvider()
        provider.setLocale("th", thaiBundle)
        provider.setOverrides(mapOf("amity_social_greeting" to "Hi %s, you have %d new items"))

        assertEquals(
            "Hi Alice, you have 3 new items",
            provider.getString("amity_social_greeting", "Alice", 3)
        )
    }
}

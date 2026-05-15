package com.amity.socialcloud.uikit.common.localization

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Integration tests for AmityCommonStringProvider with a full Italian locale bundle.
 *
 * Validates that the resolution chain works end-to-end with Italian translations,
 * including format arguments, priority rules, reaction display names, and locale lifecycle.
 */
class AmityCommonStringProviderItalianTest {

    private fun createProvider() = DefaultAmityCommonStringProvider(stringKeyToResId = emptyMap())

    private val italianBundle = mapOf(
        "amity_common_delete" to "Elimina",
        "amity_common_cancel" to "Annulla",
        "amity_common_sponsored" to "Sponsorizzato",
        "amity_common_ad_about_title" to "Informazioni su questo annuncio",
        "amity_common_ad_why_title" to "Perché vedi questo annuncio?",
        "amity_common_ad_why_description" to "Stai vedendo questo annuncio perché è stato mostrato a tutti gli utenti del sistema.",
        "amity_common_ad_about_advertiser" to "Informazioni sull'inserzionista",
        "amity_common_ad_advertiser_name" to "Nome dell'inserzionista: %s",
        "amity_common_no_reactions_yet" to "Ancora nessuna reazione",
        "amity_common_be_first_to_react" to "Sii il primo a reagire",
        "amity_common_unable_to_load_content" to "Impossibile caricare il contenuto",
        "amity_common_all" to "Tutti",
        "amity_common_just_now" to "Proprio ora",
        "amity_common_sign_in_to_continue" to "Accedi per continuare",
        "amity_common_join_community_to_interact" to "Unisciti alla comunità per interagire",
        "amity_common_message_not_sent" to "Messaggio non inviato",
        "amity_common_no_comments_yet" to "Ancora nessun commento",
        "amity_common_delete_story" to "Elimina storia",
        // Reaction display names
        "amity_common_reaction_like" to "Mi piace",
        "amity_common_reaction_love" to "Amore",
        "amity_common_reaction_fire" to "Fuoco",
        "amity_common_reaction_happy" to "Felice",
        "amity_common_reaction_sad" to "Triste",
    )

    // --- Italian locale bundle tests ---

    @Test
    fun italianLocale_resolvesAllKeys() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        assertEquals("Elimina", provider.getString("amity_common_delete"))
        assertEquals("Annulla", provider.getString("amity_common_cancel"))
        assertEquals("Sponsorizzato", provider.getString("amity_common_sponsored"))
        assertEquals("Tutti", provider.getString("amity_common_all"))
        assertEquals("Proprio ora", provider.getString("amity_common_just_now"))
    }

    @Test
    fun italianLocale_adStringsResolveCorrectly() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        assertEquals("Informazioni su questo annuncio", provider.getString("amity_common_ad_about_title"))
        assertEquals("Perché vedi questo annuncio?", provider.getString("amity_common_ad_why_title"))
        assertEquals("Informazioni sull'inserzionista", provider.getString("amity_common_ad_about_advertiser"))
    }

    @Test
    fun italianLocale_formatStringWithAdvertiserName() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        assertEquals(
            "Nome dell'inserzionista: Gucci",
            provider.getString("amity_common_ad_advertiser_name", "Gucci")
        )
    }

    @Test
    fun italianLocale_keyNotInBundle_fallsToKeyName() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        assertEquals("amity_common_unknown_key", provider.getString("amity_common_unknown_key"))
    }

    // --- Override takes priority over Italian locale ---

    @Test
    fun overrideTakesPriorityOverItalianLocale() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        provider.setOverrides(mapOf("amity_common_delete" to "Remove"))

        assertEquals("Remove", provider.getString("amity_common_delete"))
        // Non-overridden key still resolves from Italian locale
        assertEquals("Annulla", provider.getString("amity_common_cancel"))
    }

    @Test
    fun clearOverrides_fallsBackToItalianLocale() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        provider.setOverrides(mapOf("amity_common_delete" to "Remove"))
        assertEquals("Remove", provider.getString("amity_common_delete"))

        provider.clearOverrides()
        assertEquals("Elimina", provider.getString("amity_common_delete"))
    }

    // --- Italian locale lifecycle ---

    @Test
    fun clearItalianLocale_fallsToKeyFallback() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))

        provider.clearLocale()
        assertEquals("amity_common_delete", provider.getString("amity_common_delete"))
    }

    @Test
    fun switchFromItalianToJapanese() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))

        val japaneseBundle = mapOf(
            "amity_common_delete" to "削除",
            "amity_common_cancel" to "キャンセル",
        )
        provider.setLocale("ja", japaneseBundle)
        assertEquals("削除", provider.getString("amity_common_delete"))
        assertEquals("キャンセル", provider.getString("amity_common_cancel"))
    }

    @Test
    fun switchBetweenItalianAndThai() {
        val provider = createProvider()

        val thaiBundle = mapOf(
            "amity_common_delete" to "ลบ",
            "amity_common_cancel" to "ยกเลิก",
        )

        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))
        assertEquals("Annulla", provider.getString("amity_common_cancel"))

        provider.setLocale("th", thaiBundle)
        assertEquals("ลบ", provider.getString("amity_common_delete"))
        assertEquals("ยกเลิก", provider.getString("amity_common_cancel"))

        // Switch back to Italian
        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))
    }

    // --- Full priority chain with Italian ---

    @Test
    fun fullPriorityChain_overrideWinsOverItalianLocaleWinsOverFallback() {
        val provider = createProvider()

        // Step 1: No locale, no override → key fallback
        assertEquals("amity_common_delete", provider.getString("amity_common_delete"))

        // Step 2: Italian locale → Italian value
        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))

        // Step 3: Override → override value
        provider.setOverrides(mapOf("amity_common_delete" to "Custom Delete"))
        assertEquals("Custom Delete", provider.getString("amity_common_delete"))

        // Step 4: Clear override → falls back to Italian locale
        provider.clearOverrides()
        assertEquals("Elimina", provider.getString("amity_common_delete"))

        // Step 5: Clear locale → falls back to key name
        provider.clearLocale()
        assertEquals("amity_common_delete", provider.getString("amity_common_delete"))
    }

    @Test
    fun italianLocale_emptyStringOverrideStillWins() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        provider.setOverrides(mapOf("amity_common_delete" to ""))

        assertEquals("", provider.getString("amity_common_delete"))
    }

    @Test
    fun italianLocale_setLocaleReplacesEntireBundle() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        assertEquals("Elimina", provider.getString("amity_common_delete"))
        assertEquals("Annulla", provider.getString("amity_common_cancel"))

        // Replace with smaller bundle
        provider.setLocale("it", mapOf("amity_common_delete" to "Rimuovi"))
        assertEquals("Rimuovi", provider.getString("amity_common_delete"))
        // amity_common_cancel no longer in bundle → key fallback
        assertEquals("amity_common_cancel", provider.getString("amity_common_cancel"))
    }

    @Test
    fun italianLocale_formatArgsWithOverrideTakesPriority() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        provider.setOverrides(mapOf("amity_common_ad_advertiser_name" to "Advertiser: %s"))

        assertEquals(
            "Advertiser: Nike",
            provider.getString("amity_common_ad_advertiser_name", "Nike")
        )
    }

    // --- Reaction display names in Italian ---

    @Test
    fun italianLocale_reactionDisplayNames() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        assertEquals("Mi piace", provider.getString("amity_common_reaction_like"))
        assertEquals("Amore", provider.getString("amity_common_reaction_love"))
        assertEquals("Fuoco", provider.getString("amity_common_reaction_fire"))
        assertEquals("Felice", provider.getString("amity_common_reaction_happy"))
        assertEquals("Triste", provider.getString("amity_common_reaction_sad"))
    }

    @Test
    fun italianLocale_overrideReactionDisplayName() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)
        provider.setOverrides(mapOf("amity_common_reaction_like" to "Bello"))

        assertEquals("Bello", provider.getString("amity_common_reaction_like"))
        // Non-overridden reactions still use Italian locale
        assertEquals("Amore", provider.getString("amity_common_reaction_love"))
    }

    @Test
    fun italianLocale_unknownReaction_fallsToKey() {
        val provider = createProvider()
        provider.setLocale("it", italianBundle)

        // Custom reaction not in Italian bundle → key fallback
        assertEquals(
            "amity_common_reaction_celebrate",
            provider.getString("amity_common_reaction_celebrate")
        )
    }
}

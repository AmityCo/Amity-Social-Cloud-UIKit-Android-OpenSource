package com.amity.socialcloud.uikit.common.extionsions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [extractUrls] / [String.extractUrls] covering the URL pattern
 * matching requirements from PDT-2228 (row 4 of the QA test table).
 *
 * Test data sourced directly from the Jira ticket:
 *
 * ❌ ://example.com         → Not matched (invalid protocol)
 * ❌ ///example.com         → Not matched (too many slashes)
 * ❌ my_http://test         → Not matched (preceded by underscore)
 * ❌ test://example         → Not matched (unregistered schema)
 * ❌ http://example.com/path(1 → Unbalanced paren; paren and beyond stripped
 * ❌ mailto:t.com           → Not matched (invalid mailto: tail)
 * ❌ 1.bar                  → Not matched (no recognised schema / digit-first)
 * ✅ www.google:-t          → Matched only on "www.google"
 *
 * Also covers positive cases from rows 1–3:
 * ✅ www.youtube.com        → Matched (has og:image → card shown)
 * ✅ www.madum.com          → Matched (no og:image → card hidden by imageUrl guard)
 * ✅ http://example.com/robots.txt → Matched (no metadata → card hidden)
 */
class StringExtensionsTest {

    // ── Negative cases (row 4 ❌) ─────────────────────────────────────────────

    @Test
    fun `invalid protocol - colon slashes before domain - not matched`() {
        val result = "://example.com".extractUrls()
        assertTrue(
            "Expected no URLs for '://example.com' but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `invalid protocol - triple slash before domain - not matched`() {
        val result = "///example.com".extractUrls()
        assertTrue(
            "Expected no URLs for '///example.com' but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `invalid protocol - underscore before http - not matched`() {
        val result = "my_http://test".extractUrls()
        assertTrue(
            "Expected no URLs for 'my_http://test' but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `invalid protocol - unregistered schema - not matched`() {
        val result = "test://example".extractUrls()
        assertTrue(
            "Expected no URLs for 'test://example' but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `invalid - mailto with short tail - not matched`() {
        val result = "mailto:t.com".extractUrls()
        assertTrue(
            "Expected no URLs for 'mailto:t.com' but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `invalid - digit-first domain - not matched`() {
        val result = "1.bar".extractUrls()
        assertTrue(
            "Expected no URLs for '1.bar' but got: $result",
            result.isEmpty()
        )
    }

    // ── Partial match (row 4 ✅ partial) ─────────────────────────────────────

    @Test
    fun `path with unbalanced open paren - whole url not matched`() {
        // Unbalanced "(" means the full token "http://example.com/path(1" is detected
        // but filtered out — nothing is returned (spec: "not balanced parens → not matched").
        val result = "http://example.com/path(1".extractUrls()
        assertTrue(
            "Expected no URLs for unbalanced paren URL but got: $result",
            result.isEmpty()
        )
    }

    @Test
    fun `path with unbalanced paren in sentence with another url - skips bad url, returns good one`() {
        // The unbalanced URL is skipped; www.google in the same text is still detected.
        val result = "http://example.com/path(1 and www.google".extractUrls()
        assertEquals(1, result.size)
        assertEquals("https://www.google", result[0].url)
    }

    @Test
    fun `www dot google colon dash t - matched only www dot google`() {
        // "www.google" is a valid host.tld pair; ":-t" is not part of a URL.
        val result = "www.google:-t".extractUrls()
        assertEquals(1, result.size)
        // extractUrls() prepends https:// to bare www. URLs
        assertEquals("https://www.google", result[0].url)
        assertEquals("www.google", result[0].originalText)
    }

    // ── Positive cases (rows 1–3) ─────────────────────────────────────────────

    @Test
    fun `www youtube com - matched`() {
        val result = "www.youtube.com".extractUrls()
        assertEquals(1, result.size)
        assertEquals("https://www.youtube.com", result[0].url)
    }

    @Test
    fun `www madum com - matched`() {
        val result = "www.madum.com".extractUrls()
        assertEquals(1, result.size)
        assertEquals("https://www.madum.com", result[0].url)
    }

    @Test
    fun `http example com robots txt - matched`() {
        val result = "http://example.com/robots.txt".extractUrls()
        assertEquals(1, result.size)
        assertEquals("http://example.com/robots.txt", result[0].url)
    }

    @Test
    fun `path with balanced parens - matched in full`() {
        // Balanced "(1)" in path should be captured fully per spec.
        val result = "http://example.com/path(1)".extractUrls()
        assertEquals(1, result.size)
        assertEquals("http://example.com/path(1)", result[0].url)
    }

    // ── Extra positive cases ───────────────────────────────────────────────────

    @Test
    fun `https url with path and query - matched in full`() {
        val url = "https://www.example.com/search?q=hello+world"
        val result = url.extractUrls()
        assertEquals(1, result.size)
        assertEquals(url, result[0].url)
    }

    @Test
    fun `url embedded in sentence - extracted correctly`() {
        val result = "Check out https://example.com for more info.".extractUrls()
        assertEquals(1, result.size)
        assertEquals("https://example.com", result[0].url)
    }

    @Test
    fun `email address - domain part not linkified`() {
        // "@" is in the lookbehind so "example.com" after "@" is NOT matched.
        val result = "contact user@example.com here".extractUrls()
        assertTrue(
            "Email domain should not be extracted as URL, got: $result",
            result.isEmpty()
        )
    }
}

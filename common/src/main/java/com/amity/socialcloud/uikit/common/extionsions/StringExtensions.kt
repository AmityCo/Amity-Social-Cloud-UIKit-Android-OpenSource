package com.amity.socialcloud.uikit.common.extionsions

import android.util.Log
import java.util.regex.Pattern

private val urlPattern: Pattern = Pattern.compile(
    // Negative lookbehind prevents matching mid-word, after "://" (invalid protocol),
    // "///" (too many slashes), "mailto:" (colon prefix), or "@" (email addresses).
    // Domain must start with a letter (blocks "1.bar").
    // TLD must be 2+ letters only (no digit-only suffixes).
    // Path captures everything including unbalanced parens — filtering happens in code.
    """(?<![a-zA-Z0-9/:.@])((?:https?://|www\.)?(?:[a-zA-Z][-a-zA-Z0-9]*\.)+[a-zA-Z]{2,}(?:/[^?\s]*)?(?:\?[^\s]*)?)""",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE
)

data class UrlPosition(
    val url: String,
    val start: Int,
    val end: Int,
    val originalText: String = url
)

fun String.hasBalancedParens(): Boolean =
    count { it == '(' } == count { it == ')' }

fun String.extractUrls(): List<UrlPosition> {
    val matcher = urlPattern.matcher(this)
    var matchStart: Int
    var matchEnd: Int
    val links = arrayListOf<UrlPosition>()

    while (matcher.find()) {
        matchStart = matcher.start()
        matchEnd = matcher.end()

        val matchedText = this.substring(matchStart, matchEnd)

        // Skip URLs with unbalanced parentheses (spec: "path(1" → not matched)
        if (!matchedText.hasBalancedParens()) continue

        var url = matchedText
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://") && !url.startsWith("mailto:"))
            url = "https://$url"

        links.add(UrlPosition(url, matchStart, matchEnd, matchedText))
    }
    return links
}

fun String.parseUrls() : String {
    return if (!this.startsWith("http://") && !this.startsWith("https://") && !this.startsWith("ftp://") && !this.startsWith("mailto:"))
        "https://$this"
    else
        this

}
package com.amity.socialcloud.uikit.common.extionsions

import android.util.Log
import java.util.regex.Pattern

private val urlPattern: Pattern = Pattern.compile(
    "(?<![\\w])(?:(?:https?|ftp):\\/\\/(?:[a-zA-Z0-9.-]+|[\\d.]+)(?::\\d{1,5})?(?:\\/(?:[^\\s<>|()]*(?:\\([^\\s<>|()]*\\)[^\\s<>|()]*)*)*)?|mailto:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}|www\\.(?:[a-zA-Z0-9.-]+)(?:\\/(?:[^\\s<>|()]*(?:\\([^\\s<>|()]*\\)[^\\s<>|()]*)*)*)?)?(?=[.,;]?\\s|[.,;]?$|$)",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE
)

data class UrlPosition(
    val url: String,
    val start: Int,
    val end: Int,
    val originalText: String = url
)

fun String.extractUrls(): List<UrlPosition> {
    val matcher = urlPattern.matcher(this)
    var matchStart: Int
    var matchEnd: Int
    val links = arrayListOf<UrlPosition>()

    while (matcher.find()) {
        matchStart = matcher.start()
        matchEnd = matcher.end()

        val matchedText = this.substring(matchStart, matchEnd)
        
        // Skip if the match is empty or just whitespace
        if (matchedText.isBlank()) {
            continue
        }
        
        var url = matchedText
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://") && !url.startsWith("mailto:"))
            url = "https://$url"

        links.add(UrlPosition(url, matchStart, matchEnd, matchedText))
    }
    return links
}

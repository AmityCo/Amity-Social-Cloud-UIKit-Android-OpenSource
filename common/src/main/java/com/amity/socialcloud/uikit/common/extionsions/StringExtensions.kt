package com.amity.socialcloud.uikit.common.extionsions

import java.util.regex.Pattern

private val urlPattern: Pattern = Pattern.compile(
    "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
)

data class UrlPosition(
    val url: String,
    val start: Int,
    val end: Int
)

fun String.extractUrls(): List<UrlPosition> {
    val matcher = urlPattern.matcher(this)
    var matchStart: Int
    var matchEnd: Int
    val links = arrayListOf<UrlPosition>()

    while (matcher.find()) {
        matchStart = matcher.start(1)
        matchEnd = matcher.end()

        var url = this.substring(matchStart, matchEnd)
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://$url"

        links.add(UrlPosition(url, matchStart, matchEnd))
    }
    return links
}

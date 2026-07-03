package com.amity.socialcloud.uikit.chat.compose.message.element

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URI

data class LinkPreviewData(
    val url: String,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val host: String,
)

object AmityLinkPreviewFetcher {

    private val cache = LinkedHashMap<String, LinkPreviewData?>(32, 0.75f, true)
    private const val MAX_CACHE_SIZE = 80

    private val urlRegex = Regex(
        """(?:https?://|www\.)[^\s<>"{}|\\^`\[\]]+""",
        RegexOption.IGNORE_CASE,
    )

    fun extractFirstUrl(text: String): String? {
        return urlRegex.find(text)?.value
    }

    fun getCached(url: String): LinkPreviewData? = cache[url]

    fun isCached(url: String): Boolean = cache.containsKey(url)

    suspend fun fetchPreview(url: String): LinkPreviewData? {
        cache[url]?.let { return it }

        return withContext(Dispatchers.IO) {
            try {
                val normalizedUrl = if (!url.startsWith("http")) "https://$url" else url
                val doc = Jsoup.connect(normalizedUrl)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0")
                    .followRedirects(true)
                    .get()

                val ogTitle = doc.selectFirst("meta[property=og:title]")?.attr("content")
                val ogDesc = doc.selectFirst("meta[property=og:description]")?.attr("content")
                val ogImage = doc.selectFirst("meta[property=og:image]")?.attr("content")

                val title = ogTitle
                    ?: doc.selectFirst("meta[name=title]")?.attr("content")
                    ?: doc.title().takeIf { it.isNotBlank() }

                val description = ogDesc
                    ?: doc.selectFirst("meta[name=description]")?.attr("content")

                val imageUrl = ogImage
                    ?: doc.selectFirst("meta[property=og:image:url]")?.attr("content")
                    ?: run {
                        // Favicon fallback when no OG image is present
                        val uri = URI(normalizedUrl)
                        val faviconUrl = "${uri.scheme}://${uri.host}/favicon.ico"
                        faviconUrl
                    }

                val host = try {
                    val uri = URI(normalizedUrl)
                    uri.host?.removePrefix("www.") ?: normalizedUrl
                } catch (_: Exception) {
                    normalizedUrl
                }

                val data = LinkPreviewData(
                    url = normalizedUrl,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    host = host,
                )

                synchronized(cache) {
                    if (cache.size >= MAX_CACHE_SIZE) {
                        cache.remove(cache.keys.first())
                    }
                    cache[url] = data
                }

                data
            } catch (_: Exception) {
                synchronized(cache) {
                    cache[url] = null
                }
                null
            }
        }
    }
}

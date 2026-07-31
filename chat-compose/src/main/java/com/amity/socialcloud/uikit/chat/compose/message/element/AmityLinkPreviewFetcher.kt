package com.amity.socialcloud.uikit.chat.compose.message.element

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    /** Prepend a scheme so a bare-domain match (`www.…`) resolves when opened. */
    fun normalizeUrl(url: String): String = if (!url.startsWith("http")) "https://$url" else url

    fun extractFirstUrl(text: String): String? = text.extractUrls().firstOrNull()?.url

    fun getCached(url: String): LinkPreviewData? = cache[url]

    fun isCached(url: String): Boolean = cache.containsKey(url)

    suspend fun fetchPreview(url: String): LinkPreviewData? {
        cache[url]?.let { return it }

        return withContext(Dispatchers.IO) {
            val normalizedUrl = normalizeUrl(url)
            try {
                val metadata = AmityCoreClient.getLinkPreviewMetadata(normalizedUrl).blockingGet()

                val host = metadata.getDomain() ?: try {
                    val uri = URI(normalizedUrl)
                    uri.host?.removePrefix("www.") ?: normalizedUrl
                } catch (_: Exception) {
                    normalizedUrl
                }

                val data = LinkPreviewData(
                    url = normalizedUrl,
                    title = metadata.getTitle(),
                    description = metadata.getDescription(),
                    imageUrl = metadata.getImageUrl(),
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

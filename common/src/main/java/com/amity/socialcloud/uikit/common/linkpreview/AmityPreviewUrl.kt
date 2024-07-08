package com.amity.socialcloud.uikit.common.linkpreview

import android.util.Patterns
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataCacheItem
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataException
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewNoUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewUrlCacheItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.joda.time.DateTime
import org.jsoup.Jsoup

object AmityPreviewUrl {

    private val postUrlCache: MutableMap<String, AmityPreviewUrlCacheItem> = mutableMapOf()
    private val commentUrlCache: MutableMap<String, AmityPreviewUrlCacheItem> = mutableMapOf()
    private val previewMetadataCache: MutableMap<String, AmityPreviewMetadataCacheItem> =
        mutableMapOf()

    fun getPostPreviewUrl(
        postId: String,
        postUrl: String?,
        editedAt: DateTime?
    ): AmityPreviewUrlCacheItem? {
        val cache = getPostUrlFromCache(postId)
        if (cache == null) {
            //  empty cache, find the URL from post text
            // val url = findFirstUrl(postText)
            savePostUrlInCache(postId, postUrl, editedAt)
        } else if (editedAt?.isAfter(cache.editedAt) == true) {
            //  post is edited after cache, find the URL from post text
            savePostUrlInCache(postId, postUrl, editedAt)
        } else {
            //  post is not edited after cache or cache has no URL, do nothing
        }
        return postUrlCache[postId]
    }

    fun getCommentPreviewUrl(
        commentId: String,
        commentUrl: String?,
        editedAt: DateTime?
    ): AmityPreviewUrlCacheItem? {
        val cache = getCommentUrlFromCache(commentId)
        if (cache == null) {
            saveCommentUrlInCache(commentId, commentUrl, editedAt)
        } else if (editedAt?.isAfter(cache.editedAt) == true) {
            saveCommentUrlInCache(commentId, commentUrl, editedAt)
        }
        return commentUrlCache[commentId]
    }

    fun fetchMetadata(url: String): Single<AmityPreviewMetadataCacheItem> {
        val cache = getPreviewFromCache(url)
        return if (cache != null) {
            Single.just(cache)
        } else {
            fetchPreviewMetadata(url)
                .doOnSuccess {
                    previewMetadataCache[url] = it
                }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getPostUrlFromCache(postId: String): AmityPreviewUrlCacheItem? {
        return postUrlCache[postId]
    }

    private fun getCommentUrlFromCache(commentId: String): AmityPreviewUrlCacheItem? {
        return commentUrlCache[commentId]
    }

    private fun findDomainName(url: String): String? {
        val matcher = Patterns.DOMAIN_NAME.matcher(url)
        return if (matcher.find()) {
            matcher.group()
        } else {
            null
        }
    }

    private fun savePostUrlInCache(postId: String, url: String?, editedAt: DateTime?) {
        if (url == null) {
            //  no URL in post text, cache empty item
            postUrlCache[postId] = AmityPreviewNoUrl()
        } else {
            //  found URL in post text, cache the URL
            postUrlCache[postId] = AmityPreviewUrlCacheItem(url, editedAt)
        }
    }

    private fun saveCommentUrlInCache(commentId: String, url: String?, editedAt: DateTime?) {
        if (url == null) {
            //  no URL in post text, cache empty item
            commentUrlCache[commentId] = AmityPreviewNoUrl()
        } else {
            //  found URL in post text, cache the URL
            commentUrlCache[commentId] = AmityPreviewUrlCacheItem(url, editedAt)
        }
    }

    private fun getPreviewFromCache(url: String): AmityPreviewMetadataCacheItem? {
        val cache = previewMetadataCache[url]
        //  check cache timestamp is older than one day
        return if (cache?.timestamp?.plusDays(1)?.isAfterNow == true) {
            cache
        } else {
            null
        }
    }

    private fun fetchPreviewMetadata(url: String): Single<AmityPreviewMetadataCacheItem> {
        val domain = findDomainName(url) ?: ""

        return Single.create { emitter ->
            try {
                val response =
                    OkHttpClient().newCall(Request.Builder().url(url).build()).execute()
                if (response.isSuccessful) {
                    val html = response.body?.string() ?: ""
                    val doc = Jsoup.parse(html)

                    val title = doc.title()
                    val metaTags = doc.select("meta[property=og:image]")
                    val imageUrl = metaTags.attr("content")

                    emitter.onSuccess(
                        AmityPreviewMetadataCacheItem(
                            url = url,
                            domain = domain,
                            title = title,
                            imageUrl = imageUrl,
                            timestamp = DateTime.now()
                        )
                    )
                } else {
                    emitter.onError(AmityPreviewMetadataException(url))
                }
            } catch (e: Exception) {
                emitter.onError(AmityPreviewMetadataException(url))
            }
        }
    }
}
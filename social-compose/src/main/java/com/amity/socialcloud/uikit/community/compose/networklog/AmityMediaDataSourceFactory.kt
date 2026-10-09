package com.amity.socialcloud.uikit.community.compose.networklog

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivityType
import com.amity.socialcloud.uikit.common.infra.networklog.AmityNetworkCaptureInterceptor
import okhttp3.OkHttpClient

/**
 * Builds the HTTP data source ExoPlayer uses for HLS manifests and segments.
 *
 * Replaces `DefaultHttpDataSource`, which is backed by `HttpURLConnection` and therefore
 * invisible to any OkHttp interceptor. `media3-datasource-okhttp` was already a declared
 * dependency of this project and entirely unused, so this costs no new dependency.
 *
 * Segment traffic is high volume — a five-minute livestream is on the order of 1,500
 * requests — so the collector groups these rather than listing them individually.
 */
object AmityMediaDataSourceFactory {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AmityNetworkCaptureInterceptor(AmityNetworkActivityType.MEDIA_SEGMENT))
            .build()
    }

    @OptIn(UnstableApi::class)
    fun create(headers: Map<String, String>? = null): HttpDataSource.Factory {
        val factory = OkHttpDataSource.Factory(client)
        if (!headers.isNullOrEmpty()) {
            factory.setDefaultRequestProperties(headers)
        }
        return factory
    }
}

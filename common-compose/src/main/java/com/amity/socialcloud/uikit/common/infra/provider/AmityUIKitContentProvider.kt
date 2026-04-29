package com.amity.socialcloud.uikit.common.infra.provider

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.infra.android.BaseInitProvider
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import com.amity.socialcloud.uikit.common.infra.initializer.AmityUIKitInitializer
import okhttp3.OkHttpClient
import java.util.concurrent.atomic.AtomicBoolean


class AmityUIKitContentProvider : BaseInitProvider(), SingletonImageLoader.Factory {
    override fun onCreate(): Boolean {
        AmityUIKitInitializer.init(context!!)
        isInitialized.set(true)
        return true
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        // OkHttpClient with auth interceptor for Amity-hosted images (NETWORK access type).
        // Bearer token is added only for hosts ending in ".amity.co" so it never leaks to
        // external CDNs (e.g. Mux stream.mux.com).
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val host = request.url.host
                val newRequest = if (host.endsWith(".amity.co") || host == "amity.co") {
                    val token = AmityCoreClient.getAccessToken() ?: ""
                    if (token.isNotEmpty()) {
                        request.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                    } else {
                        request
                    }
                } else {
                    request
                }
                chain.proceed(newRequest)
            }
            .build()

        return ImageLoader.Builder(AmityAppContext.getContext())
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = okHttpClient))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.9)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }

    companion object {
        private val isInitialized = AtomicBoolean(false)
        fun isInitialized(): Boolean {
            return isInitialized.get()
        }
    }
}
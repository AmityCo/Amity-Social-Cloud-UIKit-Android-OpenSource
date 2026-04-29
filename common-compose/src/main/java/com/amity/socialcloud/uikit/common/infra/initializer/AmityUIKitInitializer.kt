package com.amity.socialcloud.uikit.common.infra.initializer

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import okhttp3.OkHttpClient


object AmityUIKitInitializer {

    @Synchronized
    fun init(context: Context) {
        AmityAppContext.init(context)
        initCoilImageLoader(context)
    }

    private fun initCoilImageLoader(context: Context) {
        // OkHttpClient with auth interceptor for Amity-hosted images (NETWORK access type).
        // Bearer token is added only for hosts ending in ".amity.co" so it never leaks to
        // external CDNs (e.g. Mux stream.mux.com).
        // This covers both upload.*.amity.co (file storage) and apix.*.amity.co
        // (recorded stream thumbnails served through the API).
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val host = request.url.host
                val newRequest = if (host.endsWith(".amity.co") || host == "amity.co") {
                    val token = try { AmityCoreClient.getAccessToken() } catch (_: Exception) { null }
                    if (!token.isNullOrEmpty()) {
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

        // setSafe only sets the loader if Coil hasn't been initialized yet.
        // ContentProvider.onCreate() runs before any Activity, so this is always early enough.
        // NOTE: We cannot rely on SingletonImageLoader.Factory / ServiceLoader auto-discovery
        // because there is no META-INF/services/coil3.SingletonImageLoader$Factory in the
        // project — so newImageLoader() in AmityUIKitContentProvider is never called by Coil.
        // Calling setSafe here is the guaranteed path.
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(context)
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
                .build()
        }
    }
}
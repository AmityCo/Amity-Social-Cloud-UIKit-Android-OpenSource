package com.amity.socialcloud.uikit.common.infra.provider

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.util.DebugLogger
import com.amity.socialcloud.sdk.infra.android.BaseInitProvider
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import com.amity.socialcloud.uikit.common.infra.initializer.AmityUIKitInitializer
import java.util.concurrent.atomic.AtomicBoolean


class AmityUIKitContentProvider : BaseInitProvider(), SingletonImageLoader.Factory {
    override fun onCreate(): Boolean {
        AmityUIKitInitializer.init(context!!)
        isInitialized.set(true)
        return true
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {

        return ImageLoader.Builder(AmityAppContext.getContext())
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context,0.9)
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
package com.amity.socialcloud.uikit.community.compose.screenshot.base

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * A test-only [ViewModelStoreOwner] that serves pre-built Fake ViewModel instances
 * keyed by their class. Used to override [LocalViewModelStoreOwner] in Compose
 * screenshot tests so that [AmityPostDetailPage] receives Fake ViewModels instead
 * of real ones that call SDK singletons.
 *
 * If a class is NOT found in [viewModels], [fallbackFactory] is tried next.
 * This allows deeply nested composables (e.g. [AmityPostPollElement]) to create their
 * own ViewModels via their custom factories without needing to be pre-registered here.
 *
 * Usage:
 *   val fakeOwner = FakeViewModelStoreOwner(
 *       mapOf(
 *           AmityPostDetailPageViewModel::class.java to FakeAmityPostDetailPageViewModel(),
 *           AmityPostMenuViewModel::class.java to FakeAmityPostMenuViewModel(),
 *           AmityCommentTrayComponentViewModel::class.java to FakeAmityCommentTrayComponentViewModel(),
 *       )
 *   )
 */
class FakeViewModelStoreOwner(
    private val viewModels: Map<Class<out ViewModel>, ViewModel>,
    private val fallbackFactory: ViewModelProvider.Factory? = null,
) : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {

    private val store = ViewModelStore()

    override val viewModelStore: ViewModelStore = store

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val prebuilt = viewModels[modelClass]
                if (prebuilt != null) return prebuilt as T

                if (fallbackFactory != null) {
                    return fallbackFactory.create(modelClass)
                }

                throw IllegalArgumentException(
                    "No fake ViewModel registered for ${modelClass.simpleName}. " +
                    "Add it to the map passed to FakeViewModelStoreOwner, or supply a fallbackFactory."
                )
            }
        }
}

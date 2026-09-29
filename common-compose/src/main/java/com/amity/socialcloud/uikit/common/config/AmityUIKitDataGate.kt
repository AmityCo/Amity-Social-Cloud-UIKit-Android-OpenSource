package com.amity.socialcloud.uikit.common.config

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * The module gate, applied where the data is asked for rather than where it is drawn.
 *
 * A hidden composable is not a silent one. Compose builds the component, the view
 * model is constructed with it, and its queries run whether or not anything ends
 * up on screen — so a surface can disappear exactly as the spec requires while the
 * app keeps talking to the server about it. That is what the api trace found:
 * with Story off the community header still asked for story targets, with Events
 * off the community profile still queried events, and with Feed off the social
 * home kept polling the notification tray once a minute.
 *
 * Putting the question here means a caller cannot forget it. An off module hands
 * back an empty result of the right shape, which every collector already handles,
 * instead of a request nobody will read.
 */
object AmityUIKitDataGate {

    fun isOn(feature: AmityUIKitFeature): Boolean =
        AmityUIKitConfigController.isFeatureEnabled(feature)

    fun <T : Any> paging(
        feature: AmityUIKitFeature,
        query: () -> Flow<PagingData<T>>,
    ): Flow<PagingData<T>> = if (isOn(feature)) query() else flowOf(PagingData.empty())

    fun <T> flow(
        feature: AmityUIKitFeature,
        empty: T,
        query: () -> Flow<T>,
    ): Flow<T> = if (isOn(feature)) query() else flowOf(empty)

    fun <T : Any> stream(
        feature: AmityUIKitFeature,
        query: () -> Flowable<T>,
    ): Flowable<T> = if (isOn(feature)) query() else Flowable.empty()

    /**
     * Clip follows Post (§6.2). Clip is not an [AmityUIKitFeature], so it has no
     * [isOn] of its own.
     */
    @AmityUIKitInternalApi
    fun isClipOn(): Boolean = AmityUIKitConfigController.isClipEnabled()

    /** As [paging], for a clip query. */
    @AmityUIKitInternalApi
    fun <T : Any> clipPaging(
        query: () -> Flow<PagingData<T>>,
    ): Flow<PagingData<T>> = if (isClipOn()) query() else flowOf(PagingData.empty())
}

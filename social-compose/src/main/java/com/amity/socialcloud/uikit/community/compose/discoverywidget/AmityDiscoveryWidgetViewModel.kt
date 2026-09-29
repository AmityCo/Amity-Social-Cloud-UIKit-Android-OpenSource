package com.amity.socialcloud.uikit.community.compose.discoverywidget

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.curatedcontent.AmityCuratedContentTopic
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.isTypeAvailable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The API's ceiling. Omitting the limit lets the network default -- 10 -- apply, which silently caps
 * the shelf far below what a topic holds; asking for the maximum lets the topic decide instead.
 */
private const val POOL_LIMIT = 100

class AmityDiscoveryWidgetViewModel(
    private val topicId: String,
    private val minVisibilityThreshold: Int,
) : AmityBaseViewModel() {

    sealed interface State {
        data object Loading : State

        /** Nothing renders. Failure and below-threshold are the same outcome by design. */
        data object Absent : State
        data class Rendered(
            val title: String,
            val posts: List<AmityPost>,
            val topic: AmityCuratedContentTopic,
        ) : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private var loaded = false

    /**
     * Page-load scoping is the caller's, and the SDK's inherited cooldown is a rate limit rather
     * than a scope -- it cannot express "once, then not again until reload". Held here so the
     * scope is the mount.
     */
    private var widgetImpressionFired = false
    private val postImpressionsFired = mutableSetOf<String>()


    /**
     * One shot, once per mount. The pool is not a live collection, and a widget that dropped below
     * threshold is meant to stay gone until the next page load rather than reappear mid-session.
     */
    fun load() {
        if (loaded) return
        loaded = true
        AmityCoreClient.newCuratedContentRepository()
            .getPool(topicId, limit = POOL_LIMIT)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { pool ->
                    val renderable = pool.posts.filter(::isRenderable)
                    _state.value = if (renderable.size >= minVisibilityThreshold) {
                        State.Rendered(
                            title = pool.topic.topicName,
                            posts = renderable,
                            topic = pool.topic,
                        )
                    } else {
                        State.Absent
                    }
                },
                { _state.value = State.Absent },
            )
            .let(::addDisposable)
    }

    /**
     * A post the card cannot draw is dropped rather than rendered as a broken tile, and dropped
     * before the threshold is counted — so a pool can fall below threshold on post types alone.
     *
     * A post whose module is switched off (a poll with Poll off, a clip with Clip off) is dropped
     * the same way, as every feed drops it at its query.
     *
     * Polls became renderable once the pool was confirmed to carry its polls collection: the
     * persister writes them to the cache, so the card's poll stream emits without a second fetch.
     */
    private fun isRenderable(post: AmityPost): Boolean = when (post.getData()) {
        is AmityPost.Data.TEXT,
        is AmityPost.Data.IMAGE,
        is AmityPost.Data.VIDEO,
        is AmityPost.Data.CLIP,
        is AmityPost.Data.POLL -> true

        else -> false
    } && post.isTypeAvailable()

    fun onWidgetVisible(topic: AmityCuratedContentTopic) {
        if (widgetImpressionFired) return
        widgetImpressionFired = true
        topic.analytics().markAsViewed()
    }

    fun onPostVisible(topic: AmityCuratedContentTopic, post: AmityPost) {
        if (!postImpressionsFired.add(post.getPostId())) return
        topic.analytics().markPostAsViewed(post.getPostId())
    }

    /** No page-load guard: repeat clicks are engagement. Never sits between tap and destination. */
    fun onPostClick(topic: AmityCuratedContentTopic, post: AmityPost) {
        topic.analytics().markPostClick(post.getPostId())
    }
}

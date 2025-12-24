package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.event.query.AmityEventOrderOption
import com.amity.socialcloud.sdk.api.social.event.query.AmityEventSortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType
import com.amity.socialcloud.sdk.model.social.event.AmityEventStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class AmityEventsComponentViewModel : ViewModel() {

    /**
     * Get live events (happening now)
     * @param originType Filter by origin type (COMMUNITY or USER) - required
     * @param originId Filter by specific origin ID (null for all)
     */
    fun getLiveEvents(
        originType: AmityEventOriginType,
        originId: String? = null
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.LIVE)
            .originType(originType)
        
        originId?.let { query.originId(it) }
        
        return query.build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    /**
     * Get upcoming events
     * @param originType Filter by origin type (COMMUNITY or USER) - required
     * @param originId Filter by specific origin ID (null for all)
     */
    fun getUpcomingEvents(
        originType: AmityEventOriginType,
        userId: String? = null
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.SCHEDULED)
            .originType(originType)
            .sortBy(AmityEventSortOption.START_TIME)
            .orderBy(AmityEventOrderOption.ASCENDING)

        userId?.let { query.userId(it) }
        
        return query.build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    /**
     * Get upcoming events for the current user only
     * @param originType Filter by origin type (COMMUNITY or USER) - required
     */
    fun getMyUpcomingEvents(originType: AmityEventOriginType): Flow<PagingData<AmityEvent>> {
        val currentUserId = AmityCoreClient.getUserId()
        return getUpcomingEvents(
            originType = originType,
            userId = currentUserId
        )
    }

    /**
     * Get past events (ended events)
     * @param originType Filter by origin type (COMMUNITY or USER) - required
     * @param userId Filter by user ID to get events created by specific user
     */
    fun getPastEvents(
        originType: AmityEventOriginType,
        userId: String? = null
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.ENDED)
            .originType(originType)
            .sortBy(AmityEventSortOption.START_TIME)
            .orderBy(AmityEventOrderOption.DESCENDING)
        
        userId?.let { query.userId(it) }
        
        return query.build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }
}

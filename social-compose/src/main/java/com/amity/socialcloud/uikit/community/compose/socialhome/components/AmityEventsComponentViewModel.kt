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
     * @param excludeOwnEvents Exclude events created by the current user
     */
    fun getLiveEvents(
        originType: AmityEventOriginType,
        originId: String? = null,
        excludeOwnEvents: Boolean = false
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.LIVE)
            .originType(originType)

        originId?.let { query.originId(it) }
        //        if (excludeOwnEvents) query.excludeOwnEvents(true)  // TEMP: local SDK lacks it

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
     * @param userId Filter by user ID. On its own this means events that user CREATED.
     * @param excludeOwnEvents Exclude events created by the current user
     * @param onlyAttendee Together with [userId], switch the filter from events that user created
     * to events that user attends, which includes an event created by someone else and accepted.
     */
    fun getUpcomingEvents(
        originType: AmityEventOriginType,
        userId: String? = null,
        excludeOwnEvents: Boolean = false,
        onlyAttendee: Boolean = false
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.SCHEDULED)
            .originType(originType)
            .sortBy(AmityEventSortOption.START_TIME)
            .orderBy(AmityEventOrderOption.ASCENDING)

        userId?.let { query.userId(it) }
        if (onlyAttendee) query.onlyAttendee(true)
        //        if (excludeOwnEvents) query.excludeOwnEvents(true)  // TEMP: local SDK lacks it

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
    /**
     * Upcoming events the current user attends: ones they created AND ones created by someone
     * else that they accepted.
     *
     * PDT-5966: this passed only [userId], which the SDK documents as "events created by specific
     * user", so an accepted event never reached My event. iOS has always sent onlyAttendee
     * alongside userId, which is why it was correct there.
     */
    fun getMyUpcomingEvents(originType: AmityEventOriginType): Flow<PagingData<AmityEvent>> {
        return getUpcomingEvents(
            originType = originType,
            userId = AmityCoreClient.getUserId(),
            onlyAttendee = true
        )
    }

    /**
     * Upcoming events the current user created. Backs the "Hosting" tab, which means authorship,
     * not attendance, so it deliberately does not pass onlyAttendee.
     */
    fun getHostingUpcomingEvents(originType: AmityEventOriginType): Flow<PagingData<AmityEvent>> {
        return getUpcomingEvents(
            originType = originType,
            userId = AmityCoreClient.getUserId()
        )
    }

    /**
     * Get past events (ended events)
     * @param originType Filter by origin type (COMMUNITY or USER) - required
     * @param userId Filter by user ID. On its own this means events that user CREATED.
     * @param onlyAttendee Together with [userId], switch to events that user attends (PDT-5966).
     */
    fun getPastEvents(
        originType: AmityEventOriginType,
        userId: String? = null,
        onlyAttendee: Boolean = false
    ): Flow<PagingData<AmityEvent>> {
        val query = AmitySocialClient.newEventRepository()
            .getEvents()
            .status(AmityEventStatus.ENDED)
            .originType(originType)
            .sortBy(AmityEventSortOption.START_TIME)
            .orderBy(AmityEventOrderOption.DESCENDING)
        
        userId?.let { query.userId(it) }
        if (onlyAttendee) query.onlyAttendee(true)

        return query.build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    /**
     * Past events the current user attended: ones they created AND ones they accepted (PDT-5966).
     */
    fun getMyPastEvents(originType: AmityEventOriginType): Flow<PagingData<AmityEvent>> {
        return getPastEvents(
            originType = originType,
            userId = AmityCoreClient.getUserId(),
            onlyAttendee = true
        )
    }

    /**
     * Past events the current user created. Backs the "Hosting" tab; authorship, not attendance.
     */
    fun getHostingPastEvents(originType: AmityEventOriginType): Flow<PagingData<AmityEvent>> {
        return getPastEvents(
            originType = originType,
            userId = AmityCoreClient.getUserId()
        )
    }
}

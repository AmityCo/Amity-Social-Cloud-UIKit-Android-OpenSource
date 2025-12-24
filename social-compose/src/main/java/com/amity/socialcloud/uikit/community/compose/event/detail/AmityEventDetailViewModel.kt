package com.amity.socialcloud.uikit.community.compose.event.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.event.AmityEventResponseStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class AmityEventDetailViewModel(
    private val eventId: String
) : ViewModel() {

    private val _eventDetailState = MutableStateFlow<EventDetailState>(EventDetailState.Loading)
    val eventDetailState: StateFlow<EventDetailState> = _eventDetailState.asStateFlow()

    private val _communityId = MutableStateFlow<String?>(null)
    val communityId: StateFlow<String?> = _communityId.asStateFlow()

    private val _excludedPostIds = MutableStateFlow<List<String>>(emptyList())
    val excludedPostIds: StateFlow<List<String>> = _excludedPostIds.asStateFlow()

    private val _currentUserId = MutableStateFlow<String>("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _isEventCreator = MutableStateFlow(false)
    val isEventCreator: StateFlow<Boolean> = _isEventCreator.asStateFlow()

    private val _hasDeleteEventPermission = MutableStateFlow(false)
    val hasDeleteEventPermission: StateFlow<Boolean> = _hasDeleteEventPermission.asStateFlow()

    init {
        _currentUserId.value = AmityCoreClient.getUserId()
    }

    fun updateEventData(event: AmityEvent?, isGoing: Boolean?) {
        event?.let {
            val targetCommunityId = it.getTargetCommunity()?.getCommunityId() ?: ""
            _communityId.value = targetCommunityId
            _excludedPostIds.value = it.getPostId()?.let { postId -> listOf(postId) } ?: emptyList()
            _isEventCreator.value = it.getCreator()?.getUserId() == _currentUserId.value
            
            // Check permissions
            if (targetCommunityId.isNotEmpty()) {
                checkDeleteEventPermission(targetCommunityId)
            }
        }
    }

    private fun checkDeleteEventPermission(communityId: String) {
        viewModelScope.launch {
            AmityCoreClient.hasPermission(AmityPermission.DELETE_EVENT)
                .atCommunity(communityId)
                .check()
                .asFlow()
                .catch { _hasDeleteEventPermission.value = false }
                .collect { hasPermission ->
                    _hasDeleteEventPermission.value = hasPermission
                }
        }
    }

    fun shouldShowMenu(isGoing: Boolean?): Boolean {
        return _isEventCreator.value || _hasDeleteEventPermission.value
    }

    /**
     * Get event details by ID
     */
    fun getEvent(): Flow<AmityEvent> {
        return AmitySocialClient.newEventRepository()
            .getEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { e ->
                _eventDetailState.value = EventDetailState.Error(e.message ?: "Unknown error")
            }
    }

    /**
     * Get community by ID
     */
    fun getCommunity(communityId: String): Flow<AmityCommunity> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    /**
     * Delete event by ID
     */
    fun deleteEvent(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AmitySocialClient.newEventRepository()
            .deleteEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onError(error) }
            )
    }

    /**
     * Create RSVP for the event
     */
    fun createRsvp(
        event: AmityEvent,
        status: AmityEventResponseStatus,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        event.createRSVP(status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onError(error) }
            )
    }

    /**
     * Update RSVP for the event
     */
    fun updateRsvp(
        event: AmityEvent,
        status: AmityEventResponseStatus,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        event.updateRSVP(status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onError(error) }
            )
    }

    /**
     * Join a community
     */
    fun joinCommunity(
        communityId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .joinCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onError(error) }
            )
    }

    sealed class EventDetailState {
        object Loading : EventDetailState()
        data class Error(val message: String) : EventDetailState()
    }
}

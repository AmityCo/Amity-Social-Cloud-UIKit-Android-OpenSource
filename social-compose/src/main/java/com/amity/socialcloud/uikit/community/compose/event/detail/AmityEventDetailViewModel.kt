package com.amity.socialcloud.uikit.community.compose.event.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.shareablelink.AmitySharableContentType
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.event.AmityEventResponseStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
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

    // Generated shareable event link, or null when the network deep-link config is not set
    // (domain or "events" pattern missing) or the fetch fails. Null => hide share actions.
    private val _eventShareUrl = MutableStateFlow<String?>(null)
    val eventShareUrl: StateFlow<String?> = _eventShareUrl.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        _currentUserId.value = AmityCoreClient.getUserId()
        fetchShareableEventLink()
    }

    /**
     * Reads the network-level shareable link configuration and builds the event link via the
     * typed SDK API (SDK 7.23.0-alpha01+). generateLink returns null when the EVENT content type
     * is not configured (empty domain or missing pattern).
     */
    private fun fetchShareableEventLink() {
        disposables.add(
            AmityCoreClient.getShareableLinkConfiguration()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { config ->
                        _eventShareUrl.value =
                            config.generateLink(AmitySharableContentType.EVENT, eventId)
                    },
                    {
                        // Treat any error as "sharing disabled" — never surface to the user.
                        _eventShareUrl.value = null
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun updateEventData(event: AmityEvent?, isGoing: Boolean?) {
        event?.let {
            val targetCommunityId = it.getTargetCommunity()?.getCommunityId() ?: ""
            _communityId.value = it.getDiscussionCommunityId()
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
                // Phase 4: a private/hidden community event the link recipient can't access can't be
                // fetched — show the "private community" fallback instead of a generic error, and
                // never reveal the event details. The SDK surfaces this as FORBIDDEN/PERMISSION_DENIED
                // or, when the event isn't visible to the user at all, ITEM_NOT_FOUND
                // (see EventLocalDataStore / EventResponseLocalDataStore).
                // NOTE: ITEM_NOT_FOUND also covers a genuinely deleted event (Plan 29 Open Question #4) —
                // QA should confirm against a real private event; refine if the two need distinct UI.
                val isNoAccess = when (AmityError.from(e)) {
                    AmityError.FORBIDDEN_ERROR,
                    AmityError.PERMISSION_DENIED,
                    AmityError.ITEM_NOT_FOUND -> true
                    else -> false
                }
                _eventDetailState.value = if (isNoAccess) {
                    EventDetailState.PrivateAccess
                } else {
                    EventDetailState.Error(e.message ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_something_went_wrong"))
                }
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
        // Link recipient has no access to a private/hidden community event.
        object PrivateAccess : EventDetailState()
    }
}

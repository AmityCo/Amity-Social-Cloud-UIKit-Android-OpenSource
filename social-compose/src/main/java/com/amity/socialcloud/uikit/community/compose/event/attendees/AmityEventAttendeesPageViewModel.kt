package com.amity.socialcloud.uikit.community.compose.event.attendees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AmityEventAttendeesPageViewModel(
    private val eventId: String
) : ViewModel() {

    private val _attendeeListState = MutableStateFlow(AttendeeListState.LOADING)
    val attendeeListState: StateFlow<AttendeeListState> = _attendeeListState.asStateFlow()

    private val _event = MutableStateFlow<AmityEvent?>(null)
    val event: StateFlow<AmityEvent?> = _event.asStateFlow()

    private val _attendees = MutableStateFlow<Flow<PagingData<AmityEventResponse>>?>(null)
    val attendees: StateFlow<Flow<PagingData<AmityEventResponse>>?> = _attendees.asStateFlow()

    init {
        loadEvent()
    }

    /**
     * Load the event from the repository
     */
    private fun loadEvent() {
        viewModelScope.launch {
            AmitySocialClient.newEventRepository()
                .getEvent(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
                .collect { event ->
                    _event.value = event
                    // Load attendees once event is loaded
                    if (event != null && _attendees.value == null) {
                        loadAttendees()
                    }
                }
        }
    }

    /**
     * Load event attendees (users who RSVP'd) using event.getRSVPs()
     */
    private fun loadAttendees() {
        _attendees.value = event.value?.getRSVPs()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.asFlow()
            ?.catch { e ->
                _attendeeListState.value = AttendeeListState.ERROR
            } ?: kotlinx.coroutines.flow.flowOf(PagingData.empty())
    }

    /**
     * Get event attendees (users who RSVP'd) using event.getRSVPs()
     */
    fun getAttendees(): Flow<PagingData<AmityEventResponse>> {
        return attendees.value ?: kotlinx.coroutines.flow.flowOf(PagingData.empty())
    }

    fun setAttendeeListState(state: AttendeeListState) {
        _attendeeListState.value = state
    }

    enum class AttendeeListState {
        LOADING,
        SUCCESS,
        EMPTY,
        ERROR
    }
}

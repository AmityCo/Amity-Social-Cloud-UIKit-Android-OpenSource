package com.amity.socialcloud.uikit.chat.compose.setting

import android.util.Log
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow as asFlowReactive
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.joda.time.Duration

class AmityGroupSettingPageViewModel(
    private val channelId: String,
) : AmityBaseViewModel() {

    private val _leaveState = MutableStateFlow<LeaveState>(LeaveState.Idle)
    val leaveState: StateFlow<LeaveState> = _leaveState

    private val _memberRoles = MutableStateFlow<Map<String, List<String>>>(mapOf())
    val memberRoles = _memberRoles.asStateFlow()

    fun getChannelFlow(): Flow<AmityChannel> {
        return AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun isModerator(): Flow<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.MUTE_CHANNEL)
            .atChannel(channelId)
            .check()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch { }
    }

    fun getMembers(): Flow<List<AmityChannelMember>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembersFromCache()
            .doOnNext { members ->
                _memberRoles.value = members.associate { member ->
                    (member.getUserId()) to (member.getRoles())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { emit(emptyList()) }
    }

    fun leaveChannel(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        _leaveState.value = LeaveState.Loading
        AmityChatClient.newChannelRepository()
            .leaveChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                _leaveState.value = LeaveState.Success
                onSuccess()
            }
            .doOnError {
                _leaveState.value = LeaveState.Error(it)
                onError(it)
            }
            .subscribe()
    }

    fun muteChannel(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newChannelRepository()
            .muteChannel(channelId = channelId, timeout = Duration.millis(-1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun unmuteChannel(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newChannelRepository()
            .unmuteChannel(channelId = channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    fun refreshNotificationState() {
        try {
            AmityCoreClient.notifications()
                .channel(channelId)
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { settings ->
                        Log.d("AmityGroupSettingPageViewModel", "Notification settings: $settings")
                        _notificationsEnabled.value = settings.isEnabled()
                    },
                    { /* ignore */ }
                )
        } catch (e: Exception) { /* ignore */ }
    }

    sealed class LeaveState {
        object Idle : LeaveState()
        object Loading : LeaveState()
        object Success : LeaveState()
        data class Error(val throwable: Throwable) : LeaveState()
    }
}

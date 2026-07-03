package com.amity.socialcloud.uikit.chat.compose.notification

import androidx.lifecycle.ViewModel
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.reactive.asFlow

class AmityGroupNotificationPreferencePageViewModel(private val channelId: String) : ViewModel() {

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    fun getChannelFlow(): Flow<AmityChannel?> {
        return try {
            AmityChatClient.newChannelRepository()
                .getChannel(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
        } catch (e: Exception) {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }

    fun refreshNotificationState() {
        try {
            AmityCoreClient.notifications()
                .channel(channelId)
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { settings ->
                        _notificationsEnabled.value = settings.isEnabled()
                    },
                    { /* ignore */ }
                )
        } catch (e: Exception) { /* ignore */ }
    }

    fun enableNotifications(onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        try {
            AmityCoreClient.notifications()
                .channel(channelId)
                .enable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { onSuccess() }
                .doOnError { onError(it) }
                .subscribe({}, { onError(it) })
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun disableNotifications(onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        try {
            AmityCoreClient.notifications()
                .channel(channelId)
                .disable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { onSuccess() }
                .doOnError { onError(it) }
                .subscribe({}, { onError(it) })
        } catch (e: Exception) {
            onError(e)
        }
    }
}

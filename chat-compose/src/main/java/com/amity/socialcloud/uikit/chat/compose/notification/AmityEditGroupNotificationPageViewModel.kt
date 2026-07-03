package com.amity.socialcloud.uikit.chat.compose.notification

import androidx.lifecycle.ViewModel
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow

class AmityEditGroupNotificationPageViewModel(private val channelId: String) : ViewModel() {

    fun getChannelFlow(): Flow<AmityChannel?> {
        return try {
            AmityChatClient.newChannelRepository()
                .getChannel(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
        } catch (e: Exception) {
            flowOf(null)
        }
    }

    fun getInitialMode(channel: AmityChannel?): AmityChannelNotificationMode {
        return when (channel?.getNotificationMode()) {
            "silent" -> AmityChannelNotificationMode.SILENT
            "subscribe" -> AmityChannelNotificationMode.SUBSCRIBE
            else -> AmityChannelNotificationMode.DEFAULT
        }
    }

    fun saveNotificationMode(
        mode: AmityChannelNotificationMode,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmityChatClient.newChannelRepository()
            .editChannel(channelId)
            .notificationMode(mode)
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }
}

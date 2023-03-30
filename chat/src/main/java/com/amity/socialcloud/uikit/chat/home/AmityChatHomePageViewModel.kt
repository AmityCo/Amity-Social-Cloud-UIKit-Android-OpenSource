package com.amity.socialcloud.uikit.chat.home

import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import io.reactivex.rxjava3.core.Completable

private const val MAX_CHANNEL_NAME_LENGTH = 90

class AmityChatHomePageViewModel : AmityBaseViewModel() {

    var recentChatItemClickListener: AmityRecentChatItemClickListener? = null
    var recentChatFragmentDelegate: AmityRecentChatFragmentDelegate? = null

    fun createChat(
        selectedMembers: MutableList<AmitySelectMemberItem>,
        onChatCreateSuccess: (String) -> Unit,
        onChatCreateFailed: () -> Unit
    ): Completable {
        return AmityChatClient.newChannelRepository()
            .createChannel(displayName = createChatDisplayName(selectedMembers = selectedMembers))
            .community()
            .userIds(userIds = selectedMembers.map { it.id })
            .build()
            .create()
            .doOnSuccess { onChatCreateSuccess.invoke(it.getChannelId()) }
            .doOnError { onChatCreateFailed.invoke() }
            .ignoreElement()
    }

    private fun createChatDisplayName(selectedMembers: MutableList<AmitySelectMemberItem>): String {
        var channelName = ""
        selectedMembers.forEachIndexed { index, amitySelectMemberItem ->
            channelName = if (index == 0) {
                amitySelectMemberItem.name
            } else {
                channelName + " ," + amitySelectMemberItem.name
            }
        }
        if (channelName.length > MAX_CHANNEL_NAME_LENGTH) {
            channelName = channelName.substring(0, MAX_CHANNEL_NAME_LENGTH)
            channelName = "$channelName..."
        }
        return channelName
    }
}
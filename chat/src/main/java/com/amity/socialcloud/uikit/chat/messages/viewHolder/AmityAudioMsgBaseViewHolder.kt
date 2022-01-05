package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityAudioMsgViewModel
import com.amity.socialcloud.uikit.common.common.AmityFileManager
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils
import com.amity.socialcloud.uikit.common.utils.AmityFileDownloadStatus

abstract class AmityAudioMsgBaseViewHolder(
    itemView: View,
    val audioMsgBaseViewModel: AmityAudioMsgViewModel,
    private val context: Context,
    private val audioPlayListener: AmityAudioPlayListener
) : AmitySelectableMessageViewHolder(itemView, audioMsgBaseViewModel, context) {

    init {
        addViewModelListener()
    }

    abstract fun getAudioViewHolder(): AmityAudioMsgBaseViewHolder

    private fun addViewModelListener() {
        audioMsgBaseViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.AUDIO_PLAYER_PLAY_CLICKED -> audioPlayListener.playAudio(
                    getAudioViewHolder()
                )
                AmityEventIdentifier.MESSAGE_DELETE_SUCCESS -> audioPlayListener.messageDeleted(event.dataObj as String)
                //EventIdentifier.SET_AUDIO_FILE_PROPERTIES -> setAudioFileProperties()
                else -> {
                }
            }
        }
    }

}
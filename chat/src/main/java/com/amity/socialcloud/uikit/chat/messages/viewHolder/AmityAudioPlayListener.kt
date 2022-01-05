package com.amity.socialcloud.uikit.chat.messages.viewHolder

interface AmityAudioPlayListener {

    fun playAudio(vh: AmityAudioMsgBaseViewHolder)

    fun messageDeleted(msgId: String)
}
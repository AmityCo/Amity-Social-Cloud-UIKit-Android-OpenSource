package com.amity.socialcloud.uikit.chat.messages.composebar

sealed class AmityChatRoomComposeBar {

    object DEFAULT : AmityChatRoomComposeBar()

    object TEXT : AmityChatRoomComposeBar()

}
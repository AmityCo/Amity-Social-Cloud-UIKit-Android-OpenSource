package com.amity.socialcloud.uikit.chat.home

import com.amity.socialcloud.uikit.chat.home.callback.AmityDirectoryFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel

class AmityChatHomePageViewModel : AmityBaseViewModel() {

    var recentChatItemClickListener: AmityRecentChatItemClickListener? = null
    var recentChatFragmentDelegate: AmityRecentChatFragmentDelegate? = null
    var directoryFragmentDelegate: AmityDirectoryFragmentDelegate? = null
}
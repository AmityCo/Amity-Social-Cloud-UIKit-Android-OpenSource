package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment

interface AmityPostFileItemClickListener {
    fun onClickFileItem(file: AmityFileAttachment)
}
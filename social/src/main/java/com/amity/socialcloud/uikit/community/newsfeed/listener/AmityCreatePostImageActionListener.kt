package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia

interface AmityCreatePostImageActionListener {
    fun onRemoveImage(postMedia: PostMedia, position: Int)
}
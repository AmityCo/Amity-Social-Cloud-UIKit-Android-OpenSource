package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.amity.socialcloud.sdk.social.feed.AmityPost

private const val SAVED_POST_ID = "SAVED_POST_ID"
class AmityPostDetailViewModel(private val savedState: SavedStateHandle) : ViewModel() , UserViewModel, PostViewModel, CommentViewModel {

    init {
        savedState.get<String>(SAVED_POST_ID)?.let { postId = it }
    }

    var postId: String = ""
        set(value) {
            savedState.set(SAVED_POST_ID, value)
            field = value
        }

    var post: AmityPost? = null

}
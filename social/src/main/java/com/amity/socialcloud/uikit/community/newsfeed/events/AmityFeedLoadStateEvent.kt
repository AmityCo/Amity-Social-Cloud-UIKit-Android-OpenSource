package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.model.core.error.AmityError

sealed class AmityFeedLoadStateEvent {

    class LOADED(private val itemCount: Int) : AmityFeedLoadStateEvent() {
        fun getItemCount(): Int {
            return itemCount
        }
    }

    class ERROR(private val error: AmityError) : AmityFeedLoadStateEvent() {
        fun getError(): AmityError {
            return error
        }
    }

    object LOADING : AmityFeedLoadStateEvent()
}
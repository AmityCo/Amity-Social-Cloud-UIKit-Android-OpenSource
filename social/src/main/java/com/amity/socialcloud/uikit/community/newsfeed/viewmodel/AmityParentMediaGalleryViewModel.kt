package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel

private const val SAVED_TARGET_TYPE = "SAVED_TARGET_TYPE"
private const val SAVED_TARGET_ID = "SAVED_TARGET_ID"
private const val SAVED_INCLUDE_DELETE = "SAVED_INCLUDE_DELETE"


class AmityParentMediaGalleryViewModel(private val savedState: SavedStateHandle) :
    AmityBaseViewModel() {


    var targetType = ""
        set(value) {
            savedState.set(SAVED_TARGET_TYPE, value)
            field = value
        }
    var targetId = ""
        set(value) {
            savedState.set(SAVED_TARGET_ID, value)
            field = value
        }
    var includeDelete = false
        set(value) {
            savedState.set(SAVED_INCLUDE_DELETE, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_TARGET_TYPE)?.let { targetType = it }
        savedState.get<String>(SAVED_TARGET_ID)?.let { targetId = it }
        savedState.get<Boolean>(SAVED_INCLUDE_DELETE)?.let { includeDelete = it }
    }
}

package com.amity.socialcloud.uikit.community.compose.post.composer

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class AmityMediaAttachmentViewModel : AmityBaseViewModel() {

    private val _postAttachmentPickerEvent by lazy {
        MutableStateFlow<AmityPostAttachmentPickerEvent>(AmityPostAttachmentPickerEvent.Initial)
    }
    val postAttachmentPickerEvent get() = _postAttachmentPickerEvent

    private val _postAttachmentAllowedPickerType by lazy {
        MutableStateFlow<AmityPostAttachmentAllowedPickerType>(AmityPostAttachmentAllowedPickerType.All)
    }
    val postAttachmentAllowedPickerType get() = _postAttachmentAllowedPickerType

    fun setPostAttachmentAllowedPickerType(type: AmityPostAttachmentAllowedPickerType) {
        viewModelScope.launch {
            _postAttachmentAllowedPickerType.value = type
        }
    }

    fun setPostAttachmentPickerEvent(event: AmityPostAttachmentPickerEvent) {
        viewModelScope.launch {
            _postAttachmentPickerEvent.value = event
            delay(500)
            _postAttachmentPickerEvent.value = AmityPostAttachmentPickerEvent.Initial
        }
    }
}
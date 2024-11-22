package com.amity.socialcloud.uikit.common.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AmityUIKitSnackbar {

    private val _snackbarMessage = MutableSharedFlow<String?>(1)
    val snackbarMessage: SharedFlow<String?> = _snackbarMessage.asSharedFlow()

    fun publishSnackbarMessage(message: String?) {
        _snackbarMessage.tryEmit(message)
    }

}
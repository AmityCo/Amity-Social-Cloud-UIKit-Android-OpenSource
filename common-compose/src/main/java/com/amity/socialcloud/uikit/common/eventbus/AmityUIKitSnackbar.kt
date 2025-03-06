package com.amity.socialcloud.uikit.common.eventbus

import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AmityUIKitSnackbar {

    private val _snackbarMessage = MutableSharedFlow<SnackbarData>(1)
    val snackbarMessage: SharedFlow<SnackbarData> = _snackbarMessage.asSharedFlow()

    private val _snackbarErrorMessage = MutableSharedFlow<SnackbarData>(1)
    val snackbarErrorMessage: SharedFlow<SnackbarData> = _snackbarErrorMessage.asSharedFlow()

    fun publishSnackbarMessage(
        message: String?,
        offsetFromBottom: Int = 0,
        scope: AmityComposeComponentScope? = null
    ) {
        _snackbarMessage.tryEmit(SnackbarData(message, offsetFromBottom, scope))
    }

    fun publishSnackbarErrorMessage(
        message: String?,
        offsetFromBottom: Int = 0,
        scope: AmityComposeComponentScope? = null
    ) {
        _snackbarErrorMessage.tryEmit(SnackbarData(message, offsetFromBottom, scope))
    }

}

data class SnackbarData(
    val message: String?,
    val offsetFromBottom: Int = 0,
    val scope: AmityComposeComponentScope? = null
)
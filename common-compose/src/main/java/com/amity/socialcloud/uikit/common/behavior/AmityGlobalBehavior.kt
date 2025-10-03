package com.amity.socialcloud.uikit.common.behavior

import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import kotlin.jvm.internal.Reflection.function



open class AmityGlobalBehavior {

    open fun handleVisitorUserAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Create an account or sign in to continue.")
    }

    open fun handleNonMemberAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Join community to interact.")
    }

    open fun handleNonFollowerAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Follow user to interact.")
    }
}
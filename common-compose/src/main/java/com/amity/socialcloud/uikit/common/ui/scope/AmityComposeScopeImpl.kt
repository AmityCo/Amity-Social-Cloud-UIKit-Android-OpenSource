package com.amity.socialcloud.uikit.common.ui.scope

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbarVisuals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class AmityComposePageScopeImpl(
    private val pageId: String,
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) : AmityComposePageScope {

    init {
        coroutineScope.launch {
            AmityUIKitSnackbar.snackbarMessage.collectLatest { message ->
                if (message != null) {
                    showSnackbar(message)
                    AmityUIKitSnackbar.publishSnackbarMessage(null)
                }
            }
        }
    }

    override fun getId(): String {
        return pageId
    }

    override fun getConfigId(): String {
        return "$pageId/*/*"
    }

    override fun getPageTheme(): AmityUIKitConfig.UIKitTheme? {
        return try {
            AmityUIKitConfigController.getTheme(getConfigId())
        } catch (e: Exception) {
            null
        }
    }

    override fun showSnackbar(
        message: String,
        @DrawableRes drawableRes: Int?,
        additionalHeight: Int,
    ) {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                AmitySnackbarVisuals(
                    message = message,
                    additionalHeight = additionalHeight,
                ).apply {
                    drawableRes?.let {
                        this.drawableRes = drawableRes
                    }
                }
            )
        }
    }

    override fun showProgressSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                AmityProgressSnackbarVisuals(
                    message = message,
                )
            )
        }
    }
}

internal class AmityComposeComponentScopeImpl(
    private val pageScope: AmityComposePageScope? = null,
    private val componentId: String,
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) : AmityComposeComponentScope {

    private val pageId = pageScope?.getId() ?: "*"

    override fun getId(): String {
        return componentId
    }

    override fun getConfigId(): String {
        return "$pageId/$componentId/*"
    }

    override fun getComponentTheme(): AmityUIKitConfig.UIKitTheme? {
        return try {
            AmityUIKitConfigController.getTheme(getConfigId())
        } catch (e: Exception) {
            null
        }
    }

    override fun showSnackbar(
        message: String,
        drawableRes: Int?,
        additionalHeight: Int,
    ) {
        if (pageScope == null) {
            coroutineScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    AmitySnackbarVisuals(
                        message = message,
                        additionalHeight = additionalHeight,
                    ).apply {
                        drawableRes?.let {
                            this.drawableRes = drawableRes
                        }
                    }
                )
            }
        } else {
            pageScope.showSnackbar(message, drawableRes)
        }
    }

    override fun getAccessibilityId(viewId: String): String {
        val sb = StringBuilder()
        sb.append(pageId)
        sb.append("/")
        sb.append(componentId)
        sb.append("/*")

        if (viewId.isNotEmpty()) {
            sb.append("_")
            sb.append(viewId)
        }
        return sb.toString()
    }
}

internal class AmityComposeElementScopeImpl(
    pageScope: AmityComposePageScope? = null,
    private val componentScope: AmityComposeComponentScope? = null,
    private val elementId: String,
) : AmityComposeElementScope {

    private val pageId = pageScope?.getId() ?: "*"
    private val componentId = componentScope?.getId() ?: "*"

    override fun getId(): String {
        return elementId
    }

    override fun getConfigId(): String {
        return "$pageId/$componentId/$elementId"
    }

    override fun showSnackbar(message: String, drawableRes: Int?) {
        componentScope?.showSnackbar(message, drawableRes)
    }

    override fun getAccessibilityId(viewId: String): String {
        val sb = StringBuilder()
        sb.append(pageId)
        sb.append("/")
        sb.append(componentId)
        sb.append("/")
        sb.append(elementId)

        if (viewId.isNotEmpty()) {
            sb.append("_")
            sb.append(viewId)
        }
        return sb.toString()
    }
}
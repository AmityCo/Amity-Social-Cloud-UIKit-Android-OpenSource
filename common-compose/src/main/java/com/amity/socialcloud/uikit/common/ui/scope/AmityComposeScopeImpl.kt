package com.amity.socialcloud.uikit.common.ui.scope

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import com.amity.socialcloud.uikit.common.compose.R
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
) : AmityComposePageScope, SnackbarScope {

    init {
        registerSnackBarEvents()
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

    override fun getComposeScope(): AmityComposeScope {
        return this
    }

    override fun getCoroutineScope(): CoroutineScope {
        return coroutineScope
    }

    override fun getSnackbarHostState(): SnackbarHostState {
        return snackbarHostState
    }
}

internal class AmityComposeComponentScopeImpl(
    private val pageScope: AmityComposePageScope? = null,
    private val componentId: String,
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) : AmityComposeComponentScope, SnackbarScope {

    init {
        registerSnackBarEvents()
    }

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

    override fun getComposeScope(): AmityComposeScope {
        return this
    }

    override fun getCoroutineScope(): CoroutineScope {
        return coroutineScope
    }

    override fun getSnackbarHostState(): SnackbarHostState {
        return snackbarHostState
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

interface SnackbarScope {

    fun getComposeScope(): AmityComposeScope

    fun getCoroutineScope(): CoroutineScope

    fun getSnackbarHostState(): SnackbarHostState

    fun registerSnackBarEvents() {
        getCoroutineScope().launch {
            AmityUIKitSnackbar.snackbarMessage.collectLatest { data ->
                if (data.message != null && (data.scope == null || data.scope == getComposeScope())) {
                    showSnackbar(
                        message = data.message,
                        drawableRes = R.drawable.amity_ic_snack_bar_success,
                        additionalHeight = data.offsetFromBottom,
                    )
                    AmityUIKitSnackbar.publishSnackbarMessage(null)
                }
            }
        }
        getCoroutineScope().launch {
            AmityUIKitSnackbar.snackbarErrorMessage.collectLatest { data ->
                if (data.message != null && (data.scope == null || data.scope == getComposeScope())) {
                    showErrorSnackbar(
                        message = data.message,
                        drawableRes = R.drawable.amity_ic_snack_bar_warning,
                        additionalHeight = data.offsetFromBottom
                    )
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(null)
                }
            }
        }
    }

    fun showSnackbar(
        message: String,
        @DrawableRes drawableRes: Int? = R.drawable.amity_ic_snack_bar_success,
        additionalHeight: Int = 0,
    ) {
        getCoroutineScope().launch {
            getSnackbarHostState().currentSnackbarData?.dismiss()
            getSnackbarHostState().showSnackbar(
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

    fun showErrorSnackbar(
        message: String,
        @DrawableRes drawableRes: Int? = R.drawable.amity_ic_snack_bar_warning,
        additionalHeight: Int = 0,
    ) {
        getCoroutineScope().launch {
            getSnackbarHostState().currentSnackbarData?.dismiss()
            getSnackbarHostState().showSnackbar(
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

    fun showProgressSnackbar(
        message: String,
    ) {
        getCoroutineScope().launch {
            getSnackbarHostState().currentSnackbarData?.dismiss()
            getSnackbarHostState().showSnackbar(
                AmityProgressSnackbarVisuals(
                    message = message,
                )
            )
        }
    }

    fun dismissSnackbar() {
        getCoroutineScope().launch {
            getSnackbarHostState().currentSnackbarData?.dismiss()
        }
    }

}
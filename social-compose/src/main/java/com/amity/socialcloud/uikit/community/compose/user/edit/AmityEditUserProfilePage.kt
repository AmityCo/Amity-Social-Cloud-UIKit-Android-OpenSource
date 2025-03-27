package com.amity.socialcloud.uikit.community.compose.user.edit

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.user.edit.elements.AmityEditUserAvatar
import kotlinx.coroutines.flow.catch

@Composable
fun AmityEditUserProfilePage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val viewModel = viewModel<AmityEditUserProfilePageViewModel>()
    val user by remember {
        viewModel.getUser()
    }.collectAsState(null)

    var displayName by remember(user?.getUpdatedAt()) {
        mutableStateOf(user?.getDisplayName() ?: "")
    }
    var about by remember(user?.getUpdatedAt()) {
        mutableStateOf(user?.getDescription() ?: "")
    }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    val isSaveButtonEnabled by remember(
        displayName,
        user?.getDisplayName(),
        about,
        user?.getDescription()
    ) {
        derivedStateOf {
            displayName != user?.getDisplayName() ||
                    about != (user?.getDescription() ?: "") ||
                    avatarUri != null
        }
    }

    val isKeyboardOpen by isKeyboardVisible()
    val keyboardHeight by getKeyboardHeight()
    val systemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    val shouldAllowDisplayNameEditing by remember {
        AmityCoreClient.getCoreUserSettings()
            .map {
                it.isAllowUpdateDisplayName()
            }
            .asFlow()
            .catch {
                emit(false)
            }
    }.collectAsState(false)

    val saveButtonBarBottomOffset by remember(systemBarPadding) {
        derivedStateOf {
            if (isKeyboardOpen) {
                2.dp.minus(keyboardHeight).plus(systemBarPadding).coerceAtMost(0.dp)
            } else {
                0.dp
            }
        }
    }

    var shouldDisabledClicking by remember { mutableStateOf(false) }
    var showUnsavedDialog by remember { mutableStateOf(false) }
    var showInappropriateImageDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (isSaveButtonEnabled) {
            showUnsavedDialog = true
        } else {
            context.closePage()
        }
    }

    AmityBasePage("edit_user_profile_page") {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                AmityToolBar(
                    pageScope = getPageScope(),
                    onBackClick = {
                        if (isSaveButtonEnabled) {
                            showUnsavedDialog = true
                        } else {
                            context.closePage()
                        }
                    }
                )

                Spacer(modifier.height(16.dp))
                AmityEditUserAvatar(
                    user = user,
                    modifier = modifier
                ) {
                    avatarUri = it
                }

                Spacer(modifier.height(24.dp))
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "user_display_name_title"
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.titleLegacy.copy(
                                textAlign = TextAlign.Start,
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )

                        Text(
                            text = "${displayName.length}/$UserDisplayNameLimit",
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade1
                            ),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                    AmityTextField(
                        text = displayName,
                        hint = getConfig().getText(),
                        maxCharacters = UserDisplayNameLimit,
                        enabled = shouldAllowDisplayNameEditing,
                        maxLines = 3,
                        onValueChange = {
                            displayName = it
                        },
                        modifier = modifier
                    )
                }

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier.height(24.dp))

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "user_about_title"
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                append(getConfig().getText())
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.baseShade2,
                                    )
                                ) {
                                    append(" (Optional)")
                                }
                            },
                            style = AmityTheme.typography.titleLegacy.copy(
                                textAlign = TextAlign.Start,
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )

                        Text(
                            text = "${about.length}/$UserAboutLimit",
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade1
                            ),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                    AmityTextField(
                        text = about,
                        hint = getConfig().getText(),
                        maxLines = 5,
                        maxCharacters = UserAboutLimit,
                        onValueChange = {
                            about = it
                        },
                        modifier = modifier
                    )
                }
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
                    .offset(y = saveButtonBarBottomOffset)
            ) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                )

                Spacer(modifier = modifier.height(16.dp))

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "update_user_profile_button"
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmityTheme.colors.highlight,
                            disabledContainerColor = AmityTheme.colors.baseShade4,
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        enabled = isSaveButtonEnabled,
                        modifier = modifier
                            .height(40.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            if (shouldDisabledClicking) return@Button
                            viewModel.updateUser(
                                displayName = displayName,
                                description = about,
                                avatarUri = avatarUri,
                                onSuccess = {
                                    shouldDisabledClicking = false
                                    AmityUIKitSnackbar.publishSnackbarMessage("Successfully updated your profile!")
                                    context.closePage()
                                },
                                onError = {
                                    shouldDisabledClicking = false
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to save your profile. Please try again.")
                                },
                                onInappropriateImageError = {
                                    showInappropriateImageDialog = true
                                }
                            )
                            shouldDisabledClicking = true
                        }
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = if (isSaveButtonEnabled) Color.White else AmityTheme.colors.baseShade3,
                            ),
                        )
                    }
                }
                Spacer(modifier = modifier.height(32.dp))
            }

            if (showUnsavedDialog) {
                AmityAlertDialog(
                    dialogTitle = "Unsaved changes",
                    dialogText = "Are you sure you want to discard the changes? They will be lost when you leave this page.",
                    confirmText = "Discard",
                    dismissText = "Cancel",
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        context.closePage()
                    },
                    onDismissRequest = {
                        showUnsavedDialog = false
                    }
                )
            }

            if (showInappropriateImageDialog) {
                AmityAlertDialog(
                    dialogTitle = "Inappropriate image",
                    dialogText = "Please choose a different image to upload.",
                    dismissText = "OK",
                    onDismissRequest = {
                        showInappropriateImageDialog = false
                    }
                )
            }
        }
    }
}

const val UserDisplayNameLimit = 100
const val UserAboutLimit = 180

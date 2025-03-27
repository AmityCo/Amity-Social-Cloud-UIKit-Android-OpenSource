package com.amity.socialcloud.uikit.community.compose.story.hyperlink

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@ExperimentalMaterial3Api
@Composable
fun AmityStoryHyperlinkComponent(
    modifier: Modifier = Modifier,
    defaultUrlText: String = "",
    defaultCustomText: String = "",
    onClose: (String, String) -> Unit,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityStoryHyperlinkComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val urlValidation by viewModel.urlValidation.collectAsState()
    val textValidation by viewModel.textValidation.collectAsState()

    var urlText by remember(defaultUrlText) { mutableStateOf(defaultUrlText) }
    var customText by remember(defaultCustomText) { mutableStateOf(defaultCustomText) }

    val isAllFieldsValid by remember {
        derivedStateOf {
            urlText.isNotEmpty()
        }
    }
    var isValidUrlFormat by remember { mutableStateOf(true) }
    val urlValidationError by remember(isValidUrlFormat, urlValidation) {
        derivedStateOf {
            when {
                !isValidUrlFormat -> "Please enter a valid URL."

                urlValidation is AmityStoryHyperlinkValidationUIState.Invalid &&
                        (urlValidation as AmityStoryHyperlinkValidationUIState.Invalid).data == urlText
                -> "Please enter a whitelisted URL."

                else -> ""
            }
        }
    }
    val textValidationError by remember(textValidation) {
        derivedStateOf {
            when {
                textValidation is AmityStoryHyperlinkValidationUIState.Invalid &&
                        (textValidation as AmityStoryHyperlinkValidationUIState.Invalid).data == customText
                -> "Your text contains a blocklisted word."

                else -> ""
            }
        }
    }

    val openUnsavedAlertDialog = remember { mutableStateOf(false) }
    val openRemoveLinkAlertDialog = remember { mutableStateOf(false) }

    if (openUnsavedAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = "Unsaved changes",
            dialogText = "Are you sure you want to cancel? Your changes won't be saved.",
            confirmText = "Yes",
            dismissText = "No",
            onConfirmation = { onClose(defaultUrlText, defaultCustomText) },
            onDismissRequest = { openUnsavedAlertDialog.value = false }
        )
    }
    if (openRemoveLinkAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = "Remove link?",
            dialogText = "This link will be removed from story.",
            confirmText = "Remove",
            dismissText = "Cancel",
            onConfirmation = {
                urlText = ""
                customText = ""
                onClose("", "")
            },
            onDismissRequest = { openRemoveLinkAlertDialog.value = false }
        )
    }

    LaunchedEffect(urlValidation, textValidation) {
        if (urlValidation is AmityStoryHyperlinkValidationUIState.Valid
            && textValidation is AmityStoryHyperlinkValidationUIState.Valid
        ) {
            onClose(urlText, customText)
            viewModel.resetValidation()
        }
    }

    AmityBaseComponent(componentId = "hyper_link_config_component") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .testTag("hyper_link_config_component/*")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
            ) {
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "cancel_button"
                ) {
                    Text(
                        text = "Cancel",
                        style = AmityTheme.typography.bodyLegacy,
                        modifier = modifier
                            .clickableWithoutRipple {
                                openUnsavedAlertDialog.value = true
                            }
                            .testTag(getAccessibilityId())
                    )
                }

                Text(
                    text = "Add Link",
                    style = AmityTheme.typography.titleLegacy,
                    modifier = modifier.testTag(getAccessibilityId("title_text_view"))
                )
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "done_button"
                ) {
                    Text(
                        text = "Done",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = if (isAllFieldsValid) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                        ),
                        modifier = modifier
                            .clickable(enabled = isAllFieldsValid) {
                                viewModel.validateUrls(urlText)
                                viewModel.validateTexts(customText)
                            }
                            .testTag(getAccessibilityId())
                    )
                }
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = AmityTheme.colors.divider,
            )
            Spacer(modifier = modifier.padding(top = 24.dp))

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(AmityTheme.colors.base)) {
                        append("URL")
                    }
                    withStyle(style = SpanStyle(AmityTheme.colors.alert)) {
                        append("*")
                    }
                },
                style = AmityTheme.typography.titleLegacy.copy(
                    textAlign = TextAlign.Start,
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(getAccessibilityId("hyper_link_url_title_text_view"))
            )

            AmityTextField(
                text = urlText,
                hint = "https://example.com",
                onValueChange = {
                    urlText = it
                    val matcher = Patterns.WEB_URL.matcher(it)
                    isValidUrlFormat = matcher.matches()
                },
                modifier = modifier.testTag(getAccessibilityId("hyper_link_url_text_field"))
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (urlValidationError.isEmpty()) AmityTheme.colors.divider else AmityTheme.colors.alert,
                modifier = modifier.padding(horizontal = 16.dp)
            )

            if (urlValidationError.isNotEmpty()) {
                Text(
                    text = urlValidationError,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.alert
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag(getAccessibilityId("hyper_link_url_error_text_view"))
                )
            }

            Spacer(modifier = modifier.padding(top = 24.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Customize link text",
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag(getAccessibilityId("hyper_link_custom_text_title_text_view"))
                )

                Text(
                    text = "${customText.length}/$CustomTextLimit",
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .testTag(getAccessibilityId("customize_link_text_characters_limit_text_field"))
                )
            }

            AmityTextField(
                text = customText,
                hint = "Name your link",
                maxCharacters = CustomTextLimit,
                onValueChange = {
                    customText = it
                },
                modifier = modifier.testTag(getAccessibilityId("customize_link_text_text_field"))
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (textValidationError.isEmpty()) AmityTheme.colors.divider else AmityTheme.colors.alert,
                modifier = modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = textValidationError.ifEmpty { "This text will show on the link instead of URL." },
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    color = if (textValidationError.isEmpty()) AmityTheme.colors.baseShade2
                    else AmityTheme.colors.alert
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag(getAccessibilityId("customize_link_text_description_text_field"))
            )

            if (defaultUrlText.isNotEmpty()) {
                Spacer(modifier = modifier.padding(top = 32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            openRemoveLinkAlertDialog.value = true
                        }
                        .padding(horizontal = 16.dp)
                        .testTag(getAccessibilityId("remove_link_button")),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_delete_story),
                        contentDescription = "Remove link",
                        tint = AmityTheme.colors.alert,
                        modifier = modifier.align(Alignment.CenterVertically)
                    )

                    Text(
                        text = "Remove link",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.alert
                        ),
                        modifier = modifier.testTag(getAccessibilityId("remove_link_button_text_view"))
                    )
                }

                Spacer(modifier = modifier.padding(top = 12.dp))

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

const val CustomTextLimit = 30

@ExperimentalMaterial3Api
@Preview
@Composable
fun AmityStoryHyperlinkComponentPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityStoryHyperlinkComponent(
        onClose = { _, _ -> }
    )
}
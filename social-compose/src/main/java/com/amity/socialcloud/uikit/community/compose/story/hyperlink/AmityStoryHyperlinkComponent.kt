package com.amity.socialcloud.uikit.community.compose.story.hyperlink

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.amity.socialcloud.uikit.community.compose.localization.AmitySocialStrings
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

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
    val enterValidUrlStr = amitySocialString("amity_social_label_enter_valid_url")
    val enterWhitelistedUrlStr = amitySocialString("amity_social_label_enter_whitelisted_url")
    val textContainsBlocklistedStr = amitySocialString("amity_social_label_text_contains_blocklisted")
    val urlValidationError by remember(isValidUrlFormat, urlValidation, enterValidUrlStr, enterWhitelistedUrlStr) {
        derivedStateOf {
            when {
                !isValidUrlFormat -> enterValidUrlStr

                urlValidation is AmityStoryHyperlinkValidationUIState.Invalid &&
                        (urlValidation as AmityStoryHyperlinkValidationUIState.Invalid).data == urlText
                -> enterWhitelistedUrlStr

                else -> ""
            }
        }
    }
    val textValidationError by remember(textValidation, textContainsBlocklistedStr) {
        derivedStateOf {
            when {
                textValidation is AmityStoryHyperlinkValidationUIState.Invalid &&
                        (textValidation as AmityStoryHyperlinkValidationUIState.Invalid).data == customText
                -> textContainsBlocklistedStr

                else -> ""
            }
        }
    }

    val openUnsavedAlertDialog = remember { mutableStateOf(false) }
    val openRemoveLinkAlertDialog = remember { mutableStateOf(false) }

    if (openUnsavedAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = amitySocialString("amity_social_modal_dialog_title_unsaved_changes"),
            dialogText = amitySocialString("amity_social_modal_dialog_cancel_unsaved_changes"),
            confirmText = amitySocialString("amity_social_button_yes"),
            dismissText = amitySocialString("amity_social_button_no"),
            onConfirmation = { onClose(defaultUrlText, defaultCustomText) },
            onDismissRequest = { openUnsavedAlertDialog.value = false }
        )
    }
    if (openRemoveLinkAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = amitySocialString("amity_social_modal_dialog_title_remove_link"),
            dialogText = amitySocialString("amity_social_modal_dialog_remove_story_link"),
            confirmText = amitySocialString("amity_social_button_remove"),
            dismissText = amitySocialString("amity_social_button_cancel"),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
            ) {
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "cancel_button"
                ) {
                    Text(
                        text = amitySocialString("amity_social_button_cancel"),
                        style = AmityTheme.typography.bodyLegacy,
                        modifier = Modifier
                            .clickableWithoutRipple {
                                openUnsavedAlertDialog.value = true
                            }
                            .testTag(getAccessibilityId())
                    )
                }

                Text(
                    text = amitySocialString("amity_social_button_add_link"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier.testTag(getAccessibilityId("title_text_view"))
                )
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "done_button"
                ) {
                    Text(
                        text = amitySocialString("amity_social_button_done"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = if (isAllFieldsValid) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                        ),
                        modifier = Modifier
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
            Spacer(modifier = Modifier.padding(top = 24.dp))

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag(getAccessibilityId("hyper_link_url_title_text_view"))
            )

            AmityTextField(
                text = urlText,
                hint = "https://example.com",
                innerPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                onValueChange = {
                    urlText = it
                    val matcher = Patterns.WEB_URL.matcher(it)
                    isValidUrlFormat = matcher.matches()
                },
                modifier = Modifier.testTag(getAccessibilityId("hyper_link_url_text_field"))
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (urlValidationError.isEmpty()) AmityTheme.colors.divider else AmityTheme.colors.alert,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (urlValidationError.isNotEmpty()) {
                Text(
                    text = urlValidationError,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.alert
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag(getAccessibilityId("hyper_link_url_error_text_view"))
                )
            }

            Spacer(modifier = Modifier.padding(top = 24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = amitySocialString("amity_social_label_customize_link_text"),
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
                hint = amitySocialString("amity_social_placeholder_hyperlink_name_hint"),
                innerPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                maxCharacters = CustomTextLimit,
                onValueChange = {
                    customText = it
                },
                modifier = Modifier.testTag(getAccessibilityId("customize_link_text_text_field"))
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (textValidationError.isEmpty()) AmityTheme.colors.divider else AmityTheme.colors.alert,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            val errorString = amitySocialConfigString("amity_social_label_hardcoded_hyperlinkconfig_hyperlinkconfig_ternary_this_text_will_show_on_the_link_instead_")
            Text(
                text = textValidationError.ifEmpty { errorString },
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    color = if (textValidationError.isEmpty()) AmityTheme.colors.baseShade2
                    else AmityTheme.colors.alert
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag(getAccessibilityId("customize_link_text_description_text_field"))
            )

            if (defaultUrlText.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(top = 32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
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
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(
                        text = amitySocialString("amity_social_button_remove_link"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.alert
                        ),
                        modifier = Modifier.testTag(getAccessibilityId("remove_link_button_text_view"))
                    )
                }

                Spacer(modifier = Modifier.padding(top = 12.dp))

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = AmityTheme.colors.divider,
                    modifier = Modifier.padding(horizontal = 16.dp)
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
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryViewModel
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements.AmityHyperlinkTextField
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple

@ExperimentalMaterial3Api
@Composable
fun AmityStoryHyperlinkComponent(
    modifier: Modifier = Modifier,
    defaultUrlText: String = "",
    defaultCustomText: String = "",
    onClose: () -> Unit = {},
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityDraftStoryViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var urlText by remember { mutableStateOf(defaultUrlText) }
    var customText by remember { mutableStateOf(defaultCustomText) }

    val isAllFieldsValid by remember {
        derivedStateOf {
            urlText.isNotEmpty()
        }
    }
    var isValidUrl by remember {
        mutableStateOf(true)
    }

    val openUnsavedAlertDialog = remember { mutableStateOf(false) }
    val openRemoveLinkAlertDialog = remember { mutableStateOf(false) }

    if (openUnsavedAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = "Unsaved changes",
            dialogText = "Are you sure you want to cancel? Your changes won't be saved.",
            confirmText = "Yes",
            dismissText = "No",
            onConfirmation = { onClose() },
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
                viewModel.saveStoryHyperlinkItem(
                    url = "",
                    text = ""
                )
                onClose()
            },
            onDismissRequest = { openRemoveLinkAlertDialog.value = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Cancel",
                style = AmityTheme.typography.body.copy(
                    color = AmityTheme.colors.secondary
                ),
                modifier = modifier.clickableWithoutRipple {
                    openUnsavedAlertDialog.value = true
                }
            )

            Text(
                text = "Add Link",
                style = AmityTheme.typography.title.copy(
                    color = AmityTheme.colors.secondary
                ),
            )

            Text(
                text = "Done",
                style = AmityTheme.typography.body.copy(
                    color = if (isAllFieldsValid) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                ),
                modifier = modifier.clickable(enabled = isAllFieldsValid) {
                    onClose()
                    viewModel.saveStoryHyperlinkItem(
                        url = urlText,
                        text = customText
                    )
                }
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = AmityTheme.colors.divider,
        )
        Spacer(modifier = modifier.padding(top = 24.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(AmityTheme.colors.secondary)) {
                    append("URL")
                }
                withStyle(style = SpanStyle(AmityTheme.colors.alert)) {
                    append("*")
                }
            },
            style = AmityTheme.typography.title.copy(
                textAlign = TextAlign.Start,
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        AmityHyperlinkTextField(
            text = urlText,
            hint = "https://example.com",
            onValueChange = {
                urlText = it
                val matcher = Patterns.WEB_URL.matcher(it)
                isValidUrl = matcher.matches()
            },
        )

        HorizontalDivider(
            thickness = if (isValidUrl) 0.5.dp else 1.dp,
            color = if (isValidUrl) AmityTheme.colors.divider else AmityTheme.colors.alert,
            modifier = modifier.padding(horizontal = 16.dp)
        )

        if (!isValidUrl) {
            Text(
                text = "Please enter a valid URL.",
                style = AmityTheme.typography.caption.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.alert
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
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
                style = AmityTheme.typography.title,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = "${customText.length}/$CustomTextLimit",
                style = AmityTheme.typography.caption.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.secondaryShade1
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        AmityHyperlinkTextField(
            text = customText,
            hint = "Name your link",
            maxCharacters = CustomTextLimit,
            onValueChange = {
                customText = it
            }
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = AmityTheme.colors.divider,
            modifier = modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "This text will show on the link instead of URL.",
            style = AmityTheme.typography.caption.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.secondaryShade2
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    .padding(horizontal = 16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_delete_story),
                    contentDescription = "Remove link",
                    tint = AmityTheme.colors.alert,
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onClose()
                            viewModel.saveStoryHyperlinkItem(
                                url = "",
                                text = ""
                            )
                        }
                )

                Text(
                    text = "Remove link",
                    style = AmityTheme.typography.body.copy(
                        color = AmityTheme.colors.alert
                    ),
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

const val CustomTextLimit = 30

@ExperimentalMaterial3Api
@Preview
@Composable
fun AmityStoryHyperlinkComponentPreview() {
    AmityStoryHyperlinkComponent()
}
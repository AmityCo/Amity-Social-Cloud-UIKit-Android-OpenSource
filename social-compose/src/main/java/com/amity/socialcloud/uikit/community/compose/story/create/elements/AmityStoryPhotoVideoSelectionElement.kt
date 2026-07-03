package com.amity.socialcloud.uikit.community.compose.story.create.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityCreateStorySelectionSelectedBackground
import com.amity.socialcloud.uikit.common.ui.theme.amityCreateStorySelectionSelectedText
import com.amity.socialcloud.uikit.common.ui.theme.amityCreateStorySelectionUnselectedBackground
import com.amity.socialcloud.uikit.common.ui.theme.amityCreateStorySelectionUnselectedText
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@Composable
fun AmityStoryPhotoVideoSelectionElement(
    modifier: Modifier = Modifier,
    onSelectionChange: (Boolean) -> Unit = {}
) {
    val haptics = LocalHapticFeedback.current
    var isPhotoSelected by remember { mutableStateOf(true) }

    LaunchedEffect(isPhotoSelected) {
        onSelectionChange(isPhotoSelected)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(amityCreateStorySelectionUnselectedBackground)
    ) {
        Box(
            modifier = Modifier
                .height(44.dp)
                .weight(1f)
                .clip(CircleShape)
                .background(if (isPhotoSelected) AmityTheme.colors.baseInverse else Color.Transparent)
                .clickable {
                    isPhotoSelected = true
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
        ) {
            Text(
                text = amitySocialString("amity_social_button_community_setup_image_button"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = if (isPhotoSelected) amityCreateStorySelectionSelectedText
                    else amityCreateStorySelectionUnselectedText
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("switch_mode_photo_button")
            )
        }
        Box(
            modifier = Modifier
                .height(44.dp)
                .weight(1f)
                .clip(CircleShape)
                .background(if (isPhotoSelected) Color.Transparent else AmityTheme.colors.baseInverse)
                .clickable {
                    isPhotoSelected = false
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
        ) {
            Text(
                text = amitySocialString("amity_social_button_post_composer_video_button"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = if (isPhotoSelected) amityCreateStorySelectionUnselectedText
                    else amityCreateStorySelectionSelectedText,
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("switch_mode_video_button")
            )
        }
    }
}

@Preview
@Composable
fun AmityStoryPhotoVideoSelectionElementPreview() {
    AmityStoryPhotoVideoSelectionElement()
}
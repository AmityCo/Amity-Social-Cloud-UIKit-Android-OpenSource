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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .background(Color(0xFF292B32))
    ) {
        Box(
            modifier = Modifier
                .height(44.dp)
                .weight(1f)
                .clip(CircleShape)
                .background(if (isPhotoSelected) Color(0xFFFFFFFF) else Color.Transparent)
                .clickable {
                    isPhotoSelected = true
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
        ) {
            Text(
                text = "Photo",
                fontSize = 15.sp,
                color = if (isPhotoSelected) Color(0xFF636878) else Color(0xFF898E9E),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .height(44.dp)
                .weight(1f)
                .clip(CircleShape)
                .background(if (isPhotoSelected) Color.Transparent else Color(0xFFFFFFFF))
                .clickable {
                    isPhotoSelected = false
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
        ) {
            Text(
                text = "Video",
                fontSize = 15.sp,
                color = if (isPhotoSelected) Color(0xFF898E9E) else Color(0xFF636878),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun AmityStoryPhotoVideoSelectionElementPreview() {
    AmityStoryPhotoVideoSelectionElement()
}
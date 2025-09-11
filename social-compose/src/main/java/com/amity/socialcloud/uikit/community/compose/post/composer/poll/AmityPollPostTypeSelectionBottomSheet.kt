package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPollPostTypeSelectionBottomSheet(
    modifier: Modifier = Modifier,
    onCloseSheet: () -> Unit = {},
    onNextClicked: (String) -> Unit = {}
) {
    var selectedType by remember { mutableStateOf("text") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Text(
            text = "Choose poll type",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            style = AmityTheme.typography.titleBold,
        )

        HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.baseShade4)

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AmityPollSelectionItem(
                modifier = Modifier.weight(1f),
                isSelected = selectedType == "text",
                type = "text",
                onItemClick = { selectedType = "text" }
            )

            AmityPollSelectionItem(
                modifier = Modifier.weight(1f),
                isSelected = selectedType == "image",
                type = "image",
                onItemClick = { selectedType = "image" }
            )
        }

        Spacer(Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.baseShade4)

        Button(
            onClick = {
                onNextClicked(
                    selectedType
                )
                onCloseSheet()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Next",
                style = AmityTheme.typography.bodyBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AmityPollSelectionItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    type: String = "text",
    onItemClick: () -> Unit = {}
) {
    val finalModifier = if (isSelected) modifier.border(
        width = 2.dp,
        color = AmityTheme.colors.primary,
        shape = RoundedCornerShape(8.dp)
    )
    else modifier.border(
        width = 1.dp,
        color = AmityTheme.colors.baseShade4,
        shape = RoundedCornerShape(8.dp)
    )

    val image = if (type == "text") {
        if (isSelected) painterResource(R.drawable.amity_v4_poll_type_text) else painterResource(R.drawable.amity_v4_poll_type_text_disable)
    } else {
        if (isSelected) painterResource(R.drawable.amity_v4_poll_type_image) else painterResource(R.drawable.amity_v4_poll_type_image_disable)
    }

    Column(
        modifier = finalModifier
            .aspectRatio(5f / 7f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickableWithoutRipple { onItemClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Rest of the existing content remains the same
        Box(
            modifier = Modifier
                .aspectRatio(165.5f / 184f)
                .fillMaxWidth()
                .background(if (isSelected) AmityTheme.colors.primaryShade3 else AmityTheme.colors.secondaryShade4)
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(120.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = if (type == "text") "Text-only poll" else "Image poll",
            modifier = Modifier,
            style = AmityTheme.typography.bodyBold,
            color = if (isSelected) Color(0xFF292B32) else AmityTheme.colors.baseShade1,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))
    }
}
@Preview
@Composable
private fun AmityPollPostTypeSelectionBottomSheetPreview() {
    AmityPollPostTypeSelectionBottomSheet()
}

@Preview
@Composable
private fun AmityPollSelectionItemPreview() {
    AmityPollSelectionItem(isSelected = true, type = "text")
}
package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityPendingPostActionRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
) {
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "pending_post_content"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp, bottom = 12.dp)
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "post_accept_button"
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    enabled = true,
                    modifier = modifier
                        .weight(1f)
                        .testTag(getAccessibilityId()),
                    onClick = onAccept,
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.background,
                        ),
                    )
                }
            }
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "post_decline_button"
            ) {
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),
                    border = BorderStroke(1.dp, AmityTheme.colors.baseShade3),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = modifier
                        .weight(1f)
                        .testTag(getAccessibilityId()),
                    onClick = onDecline,
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityPendingPostActionRowPreview() {
    AmityPendingPostActionRow()
}
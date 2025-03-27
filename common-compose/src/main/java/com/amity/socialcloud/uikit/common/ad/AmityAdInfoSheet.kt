package com.amity.socialcloud.uikit.common.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityAdInfoSheet(
    modifier: Modifier = Modifier,
    name: String,
    showSheet: Boolean,
    onClose: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (!showSheet) return

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            Text(
                text = "About this advertisement",
                style = AmityTheme.typography.titleLegacy,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)

            )

            HorizontalDivider(
                thickness = 0.5.dp,
                modifier = modifier.background(AmityTheme.colors.divider)
            )

            Spacer(modifier = modifier.height(24.dp))

            Text(
                text = "Why this advertisement?",
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_more_info),
                    contentDescription = null,
                    tint = AmityTheme.colors.baseShade3,
                )

                Text(
                    text = "You're seeing this advertisement because it was displayed to all users in the system.",
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.baseShade3
                    )
                )
            }

            Spacer(modifier = modifier.height(24.dp))

            Text(
                text = "About this advertiser",
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_more_info),
                    contentDescription = null,
                    tint = AmityTheme.colors.baseShade3,
                )

                Text(
                    text = "Advertiser name: $name",
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.baseShade3
                    )
                )
            }
        }
    }
}
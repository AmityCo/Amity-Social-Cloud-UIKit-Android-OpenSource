package com.amity.socialcloud.uikit.chat.compose.message.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant

@Composable
fun AmityChatReportMessageDeletedErrorContent(
    onCloseClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        AmityEmptyState(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            variant = AmityEmptyStateVariant.ICON,
            icon = CommonR.drawable.amity_ic_newspaper_question_l,
            title = amityChatString("chat.report.error.title"),
            description = amityChatString("chat.report.error.desc"),
        )

        AmityDivider(variant = AmityDividerVariant.Post)

        Spacer(modifier = Modifier.height(16.dp))

        // Fixed bottom-docked full-width footer button — a separate page-level affordance,
        // not this atom's own hug-width action slot.
        AmityButton(
            variant = AmityButtonVariant.MAIN,
            onClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = amityChatString("chat.report.error.close"),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

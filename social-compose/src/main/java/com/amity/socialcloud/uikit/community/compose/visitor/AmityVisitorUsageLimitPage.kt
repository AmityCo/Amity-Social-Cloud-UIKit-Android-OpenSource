package com.amity.socialcloud.uikit.community.compose.visitor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityVisitorUsageLimitPage(
    modifier: Modifier = Modifier,
) {
    val toastText = stringResource(R.string.amity_social_visitor_limit_toast)


    // Block system back button/gesture — user must use Sign In CTA
    BackHandler { }

    AmityBasePage(pageId = "") {
        // REQ-005: Toast shown on initial render only
        LaunchedEffect(Unit) {
            getPageScope().showSnackbar(
                toastText,
                drawableRes = R.drawable.amity_ic_snack_bar_info,
                dismissable = true
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .systemBarsPadding()
                .background(color = AmityTheme.colors.background)
        ) {
            // emptyState: 24dp padding all sides, 16dp gap between iconTextGroup and CTA
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                // iconTextGroup: icon + textGroup with 16dp gap
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // warningIcon: 64×64dp container
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_visitor_usage_limit),
                        contentDescription = null,
                        tint = AmityTheme.colors.secondaryShade4,
                        modifier = Modifier.size(64.dp)
                    )

                    // textGroup: title + subtitle stacked (0dp gap), color base/shade3
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.widthIn(max = 252.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.amity_social_visitor_limit_title),
                            style = AmityTheme.typography.titleBold.copy(
                                color = AmityTheme.colors.baseShade3,
                                textAlign = TextAlign.Center,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None,
                                ),
                            )
                        )
                        Text(
                            text = stringResource(R.string.amity_social_visitor_limit_subtitle),
                            style = AmityTheme.typography.caption.copy(
                                color = AmityTheme.colors.baseShade3,
                                textAlign = TextAlign.Center,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None,
                                ),
                            )
                        )
                    }
                }

                // ctaButton: primary-colored text, 16dp horizontal / 10dp vertical padding, 8dp radius
                TextButton(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        AmitySocialBehaviorHelper.globalBehavior.handleVisitorUsageLimitSignIn()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.amity_social_visitor_limit_sign_in),
                        style = AmityTheme.typography.bodyBold.copy(
                            color = AmityTheme.colors.primary,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}

package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper

@Composable
fun AmityPendingJoinRequestComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    joinRequest: AmityJoinRequest? = null,
    onAcceptAction: () -> Unit,
    onDeclineAction: () -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.pendingPostContentComponentBehavior
    }
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "join_request_content"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            joinRequest?.let { request ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //User Avatar
                    AmityUserAvatarView(
                        user = request.getUser(),
                        modifier = modifier.clickableWithoutRipple {
                            request.getUser()?.let {
                                behavior.goToUserProfilePage(
                                    context = context,
                                    userId = it.getUserId(),
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    //User Name
                    request.getUser()?.getDisplayName()?.let {
                        Text(
                            text = it,
                            style = AmityTheme.typography.bodyBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .clickableWithoutRipple {
                                    request.getUser()?.let {
                                        behavior.goToUserProfilePage(
                                            context = context,
                                            userId = it.getUserId(),
                                        )
                                    }
                                }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (request.getRequestorPublicId() != AmityCoreClient.getUserId()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "join_accept_button"
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmityTheme.colors.highlight,
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                ),
                                enabled = true,
                                modifier = Modifier
                                    .weight(1f),
                                onClick = {
                                    onAcceptAction.invoke()
                                },
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
                            elementId = "join_decline_button"
                        ) {
                            OutlinedButton(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                ),
                                border = BorderStroke(1.dp, AmityTheme.colors.baseShade3),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                ),
                                modifier = Modifier
                                    .weight(1f),
                                onClick = {
                                    onDeclineAction.invoke()
                                },
                            ) {
                                Text(
                                    text = getConfig().getText(),
                                    style = AmityTheme.typography.bodyBold,
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun AmityJoinRequestShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar shimmer
            Box(
                modifier = modifier
                    .size(40.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(40.dp)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Username shimmer
            Box(
                Modifier
                    .width(120.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Preview
@Composable
fun AmityJoinRequestShimmerPreview() {
    AmityJoinRequestShimmer()
}

//@Preview
//@Composable
//fun AmityPendingJoinRequestComponentPreview() {
//    AmityPendingJoinRequestComponent(
//        onAcceptAction = {},
//        onDeclineAction = {}
//    )
//}
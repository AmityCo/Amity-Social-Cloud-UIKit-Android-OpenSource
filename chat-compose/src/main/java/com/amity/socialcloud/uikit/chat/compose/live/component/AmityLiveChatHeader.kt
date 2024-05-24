package com.amity.socialcloud.uikit.chat.compose.live.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityLiveChatHeaderAvatar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityLiveChatHeader(
	pageScope: AmityComposePageScope? = null,
	viewModel: AmityLiveChatPageViewModel,
) {
	val channel by viewModel
		.getChannelFlow()
		.collectAsState(initial = null)
	
	val connection by viewModel
		.getNetworkConnectionStateFlow()
		.collectAsState(initial = NetworkConnectionEvent.Connected)
	
	AmityBaseComponent(
		componentId = "chat_header",
		pageScope = pageScope,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(16.dp, 8.dp)
		) {
			AmityLiveChatHeaderAvatar(
				pageScope = pageScope,
				componentScope = getComponentScope(),
				channel = channel,
			)
			Spacer(modifier = Modifier.width(8.dp))
			Column {
				Text(
					text = channel?.getDisplayName() ?: "",
					fontSize = 17.sp,
					lineHeight = 22.sp,
					fontWeight = FontWeight(600),
					color = AmityTheme.colors.baseInverse
				)
				Spacer(modifier = Modifier.width(8.dp))
				when (connection) {
					NetworkConnectionEvent.Connected -> {
						ChatHeaderMembersCount(
							memberCount = channel?.getMemberCount(),
							pageScope = pageScope,
						)
					}
					NetworkConnectionEvent.Disconnected -> {
						ChatHeaderWaitingForNetwork(
							pageScope = pageScope,
						)
					}
				}
				
			}
		}
	}
}


@Composable
fun ChatHeaderMembersCount(
	modifier: Modifier = Modifier,
	memberCount: Int? = null,
	pageScope: AmityComposePageScope? = null,
) {
	if (memberCount != null) {
		AmityBaseComponent(
			componentId = "member_count",
			pageScope = pageScope,
		) {
			Row(
				modifier = modifier,
				verticalAlignment = Alignment.CenterVertically
			) {
				Image(
					painter = painterResource(id = R.drawable.amity_ic_member_count),
					contentDescription = "Members count icon",
					modifier = Modifier
						.size(16.dp)
						.padding(0.dp, 0.dp, 0.dp, 2.dp)
				)
				
				Spacer(modifier = Modifier.width(4.dp))
				
				Text(
					text = "$memberCount members",
					fontSize = 13.sp,
					lineHeight = 18.sp,
					fontWeight = FontWeight(400),
					color = AmityTheme.colors.baseInverse,
				)
			}
		}
	}
}

@Composable
fun ChatHeaderWaitingForNetwork(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
) {
	AmityBaseComponent(
		componentId = "waiting_for_network",
		pageScope = pageScope,
	) {
		Row(
			modifier = modifier,
			verticalAlignment = Alignment.CenterVertically
		) {
			CircularProgressIndicator(
				modifier = Modifier
					.size(16.dp)
					.padding(top = 0.dp, bottom = 2.dp),
				color = AmityTheme.colors.baseInverse
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = "Waiting for network...",
				fontSize = 13.sp,
				lineHeight = 18.sp,
				fontWeight = FontWeight(400),
				color = AmityTheme.colors.baseInverse,
			)
		}
	}
}
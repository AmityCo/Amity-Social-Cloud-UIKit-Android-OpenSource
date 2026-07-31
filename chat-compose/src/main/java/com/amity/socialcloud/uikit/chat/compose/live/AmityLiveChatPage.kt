package com.amity.socialcloud.uikit.chat.compose.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatHeader
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatMessageList
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatMessageComposeBar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun AmityLiveChatPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val subChannelId = channelId
    val viewModel = AmityLiveChatPageViewModel(subChannelId)
    val membership by remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isBanned() == new.isBanned()
                        && old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val isChannelMuted by remember {
        viewModel.getChannelFlow().map {
            it.isMuted()
        }.distinctUntilChanged()
    }.collectAsState(initial = false)

    val isChannelModerator by remember {
        viewModel.isChannelModerator().distinctUntilChanged()
    }.collectAsState(initial = false)

    val isGlobalBanned by remember {
        viewModel.observeGlobalBanEvent().distinctUntilChanged()
    }.collectAsState(initial = false)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onStop()
        }
    }
    AmityBasePage(pageId = "live_chat_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .background(
                    getPageScope()
                        .getPageTheme()
                        ?.backgroundColor
                        ?.asColor() ?: AmityTheme.colors.background
                )
                .fillMaxSize()
                .statusBarsPadding()
                .systemBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AmityLiveChatHeader(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }
            // Resolved to Post: matches the nav/content boundary in the specced sibling AmityChatPage.
            AmityDivider(variant = AmityDividerVariant.Post)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AmityLiveChatMessageList(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }

            val showMutedLabel =
                !isChannelModerator
                        && membership?.isBanned() == false
                        && (membership?.isMuted() == true || isChannelMuted)
            if (showMutedLabel) {
                AmityDivider(variant = AmityDividerVariant.Post)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_muted),
                        contentDescription = "channel muted",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp),
                        tint = AmityTheme.colors.baseShade1,
                    )
                    val message = if (isChannelMuted) {
                        amityChatString("chat.group.permission.only.moderators.banner")
                    } else {
                        amityChatString("chat.user.is.muted")
                    }
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        text = message,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.baseShade1,
                        ),
                    )
                }
            }
            if (membership?.isBanned() == false && !isGlobalBanned)
                AmityLiveChatMessageComposeBar(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                )
        }

    }
}

@Composable
fun LoadingIndicator(itemCount: Int = 0) {
    if (itemCount == 0) {
        LoadingToast()
    } else {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            AmityLoader(
                variant = AmityLoaderVariant.Spinner,
                size = AmityLoaderSize.Sm,
                modifier = Modifier
                    .size(20.dp),
            )
        }
    }
}

@Composable
fun LoadingToast() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .shadow(4.dp)
                            .background(AmityTheme.colors.baseShade4)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(Modifier.padding(0.dp, 18.dp)) {
                                Spacer(modifier = Modifier.width(12.dp))
                                AmityLoader(
                                    variant = AmityLoaderVariant.Spinner,
                                    size = AmityLoaderSize.Sm,
                                    modifier = Modifier
                                        .size(20.dp),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = amityChatString("chat.loading.label"),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        fontWeight = FontWeight(400),
                                        color = AmityTheme.colors.baseInverse
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
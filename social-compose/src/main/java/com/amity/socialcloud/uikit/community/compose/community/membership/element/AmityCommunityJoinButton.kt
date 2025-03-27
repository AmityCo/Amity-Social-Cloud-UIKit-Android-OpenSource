package com.amity.socialcloud.uikit.community.compose.community.membership.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun AmityCommunityJoinButton(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    var isJoined by remember(community.getCommunityId()) {
        mutableStateOf(community.isJoined())
    }
    var isInProgress by remember(community.getCommunityId()) {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        AmitySocialClient.newCommunityRepository()
            .getCommunity(community.getCommunityId())
            .asFlow()
            .catch {

            }
            .collectLatest {
                isJoined = it.isJoined()
            }
    }

    if (!isJoined) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .widthIn(min = 0.dp, max = 83.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AmityTheme.colors.primary)
                .height(30.dp)
                .clickable(enabled = isInProgress.not()) {
                    coroutineScope.launch {
                        isInProgress = true
                        isJoined = true
                        val isSuccess = joinCommunity(community)
                        isInProgress = false
                        if (!isSuccess) {
                            isJoined = false
                        }
                    }

                }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Join",
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 8.dp),
                tint = Color.White
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Join",
                style = AmityTheme.typography.captionLegacy.copy(
                    color = Color.White,
                ),
            )
        }

    } else {
        Row(
            modifier = Modifier
                .widthIn(min = 0.dp, max = 83.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, AmityTheme.colors.baseShade4))
                .background(Color.Transparent)
                .height(30.dp)
                .clickable(enabled = isInProgress.not()) {
                    coroutineScope.launch {
                        isInProgress = true
                        isJoined = false
                        val isSuccess = leaveCommunity(community)
                        isInProgress = false
                        if (!isSuccess) {
                            isJoined = true
                        }
                    }

                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Leave",
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 8.dp),
                tint = AmityTheme.colors.base
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Joined",
                style = AmityTheme.typography.captionLegacy
            )
        }
    }
}

suspend fun joinCommunity(community: AmityCommunity): Boolean {
    try {
        AmitySocialClient.newCommunityRepository()
            .joinCommunity(community.getCommunityId())
            .await()
        return true
    } catch (e: Exception) {
        return false
    }
}

suspend fun leaveCommunity(community: AmityCommunity): Boolean {
    try {
        AmitySocialClient.newCommunityRepository()
            .leaveCommunity(community.getCommunityId())
            .await()
        return true
    } catch (e: Exception) {
        return false
    }
}
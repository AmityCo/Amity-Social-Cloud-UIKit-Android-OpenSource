package com.amity.socialcloud.uikit.community.compose.community.membership.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.sdk.model.social.community.AmityJoinResult
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun AmityCommunityJoinButton(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
    joinRequest: AmityJoinRequest? = null,
) {
    val behavior by lazy {
        AmitySocialBehaviorHelper.exploreComponentBehavior
    }

    var isJoined by remember(community.getCommunityId()) {
        mutableStateOf(community.isJoined())
    }
    var isInProgress by remember(community.getCommunityId()) {
        mutableStateOf(false)
    }

    var joinRequestState by remember(joinRequest) { mutableStateOf<AmityJoinRequest?>(joinRequest) }

    var isPending by remember(joinRequestState) {
        mutableStateOf(joinRequestState?.getStatus() == AmityJoinRequestStatus.PENDING)
    }

    var showLeaveCommunityDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val compositeDisposable = remember { CompositeDisposable() }

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

    if (!isJoined && !isPending) {
        //Join button UI
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .widthIn(min = 0.dp, max = 83.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AmityTheme.colors.primary)
                .height(30.dp)
                .clickable(enabled = isInProgress.not()) {
                    if(AmityCoreClient.isVisitor()){
                        behavior.handleVisitorUserAction()
                    } else {
                        community.join()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe {
                                isInProgress = true
                            }
                            .doOnSuccess { result ->
                                isInProgress = false
                                when (result) {
                                    is AmityJoinResult.Success -> {
                                        // Successfully joined the community
                                        isPending = false
                                        isJoined = true
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = "You joined ${community.getDisplayName()}.",
                                        )
                                    }

                                    is AmityJoinResult.Pending -> {
                                        // Join request is pending
                                        joinRequestState = result.request
                                        isPending = true
                                        isJoined = false
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = "Requested to join. You will be notified once your request is accepted.",
                                        )
                                    }
                                }
                            }
                            .doOnError {
                                isInProgress = false
                                isJoined = false
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                    message = "Failed to join the community. Please try again.",
                                )
                            }
                            .subscribe()
                            .apply {
                                compositeDisposable.add(this)
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

    } else if (isPending) {
        // Pending button UI
        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(6.dp))
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.secondaryShade3,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickableWithoutRipple {
                    //cancel() join request
                    joinRequestState?.let { request ->
                        val disposable = request.cancel()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe {
                                isInProgress = true
                            }
                            .doOnComplete {
                                isInProgress = false
                                isPending = false
                                isJoined = false
                            }
                            .doOnError {
                                isInProgress = false
                                AmityUIKitSnackbar.publishSnackbarMessage(
                                    message = "Failed to cancel your request. Please try again.",
                                )
                            }
                            .subscribe()

                        compositeDisposable.add(disposable)
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_clock),
                contentDescription = "Pending join community icon",
                modifier = Modifier
                    .size(20.dp),
                tint = AmityTheme.colors.secondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Pending",
                style = AmityTheme.typography.bodyBold
            )
        }
    } else {
        // Joined button UI
        Row(
            modifier = Modifier
                .widthIn(min = 0.dp, max = 83.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, AmityTheme.colors.baseShade4))
                .background(Color.Transparent)
                .height(30.dp)
                .clickable(enabled = isInProgress.not()) {
                    if (community.requiresJoinApproval()) {
                        showLeaveCommunityDialog = true
                    } else {
                        // If community does not require approval, leave directly
                        coroutineScope.launch {
                            isInProgress = true
                            isJoined = false
                            val isSuccess = leaveCommunity(community)
                            isInProgress = false
                            if (!isSuccess) {
                                isJoined = true
                                AmityUIKitSnackbar.publishSnackbarMessage(
                                    message = "Failed to leave the community. Please try again.",
                                )
                            }
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

    if (showLeaveCommunityDialog) {
        AmityAlertDialog(
            dialogTitle = "Leave community?",
            dialogText = "If you change your mind, you'll have to request to join again.",
            confirmText = "Leave",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                showLeaveCommunityDialog = false
                coroutineScope.launch {
                    isInProgress = true
                    isPending = false
                    val isSuccess = leaveCommunity(community)
                    isInProgress = false
                    if (!isSuccess) {
                        isPending = true
                        AmityUIKitSnackbar.publishSnackbarMessage(
                            message = "Failed to leave the community. Please try again.",
                        )
                    }
                }
            },
            onDismissRequest = {
                showLeaveCommunityDialog = false
            }
        )
    }

}

suspend fun joinCommunity(community: AmityCommunity): Boolean {
    try {
        AmitySocialClient.newCommunityRepository()
            .joinCommunity(community.getCommunityId())
            .await()

        AmityUIKitSnackbar.publishSnackbarMessage(
            message = "You joined ${community.getDisplayName()}.",
        )
        return true
    } catch (e: Exception) {

        AmityUIKitSnackbar.publishSnackbarErrorMessage(
            message = "Failed to join the community. Please try again.",
        )
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

@Composable
@Preview
fun TestPendingButton() {
    // This is for when complete plug with sdk need to find param to check this condition
    Row(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = AmityTheme.colors.secondaryShade3,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .clip(RoundedCornerShape(6.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_clock),
            contentDescription = "Pending join community icon",
            modifier = Modifier
                .size(20.dp),
            tint = AmityTheme.colors.secondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Pending",
            style = AmityTheme.typography.bodyBold
        )
    }
}
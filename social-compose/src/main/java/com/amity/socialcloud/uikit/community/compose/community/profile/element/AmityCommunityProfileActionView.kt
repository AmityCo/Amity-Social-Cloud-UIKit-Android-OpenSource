package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.sdk.model.social.community.AmityJoinResult
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

//@Composable
//fun AmityCommunityProfileActionView(
//    modifier: Modifier = Modifier,
//    pageScope: AmityComposePageScope? = null,
//    componentScope: AmityComposeComponentScope? = null,
//    community: AmityCommunity,
//) {
//    AmityCommunityPendingPost(
//        pageScope = pageScope,
//        componentScope = componentScope,
//        community = community
//    )
//    AmityCommunityJoinButton(
//        pageScope = pageScope,
//        componentScope = componentScope,
//        community = community
//    )
//}


@Composable
fun AmityCommunityPendingPost(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "community_pending_post"
    ) {
        val behavior = remember {
            AmitySocialBehaviorHelper.communityProfilePageBehavior
        }
        val context = LocalContext.current
        val isModerator by AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY)
            .atCommunity(community.getCommunityId())
            .check()
            .asFlow()
            .collectAsState(initial = false)

        val pendingPosts = remember {
            AmitySocialClient.newFeedRepository()
                .getCommunityFeed(community.getCommunityId())
                .reviewStatus(AmityReviewStatus.UNDER_REVIEW)
                .dataTypes(AmitySocialBehaviorHelper.supportedPostTypes)
                .includeDeleted(false)
                .matchingOnlyParentPosts(true)
                .build()
                .query()
                .asFlow()
        }.collectAsLazyPagingItems()

        val joinRequest = remember {
            community.getJoinRequests(AmityJoinRequestStatus.PENDING).asFlow()
        }.collectAsLazyPagingItems()

        val pendingPostItemCount by remember(pendingPosts) {
            derivedStateOf { pendingPosts.itemCount }
        }

        val joinRequestItemCount by remember(joinRequest) {
            derivedStateOf { joinRequest.itemCount }
        }

        val bannerContent by remember(
            pendingPostItemCount,
            joinRequestItemCount,
            isModerator,
            community
        ) {
            derivedStateOf {
                val communityRequiresJoinApproval = community.requiresJoinApproval()
                val postReviewEnabled =
                    community.getPostSettings() == AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED

                val hasPendingPosts = pendingPostItemCount > 0
                val hasPendingJoinRequests = joinRequestItemCount > 0

                // Conditions for showing each type of information
                val showPendingPostsInfo =
                    postReviewEnabled && hasPendingPosts && community.isJoined()
                val showJoinRequestsInfo =
                    communityRequiresJoinApproval && hasPendingJoinRequests && isModerator

                var title = "Pending requests"
                var desc = ""

                val postText = "${
                    if (pendingPostItemCount > 10) {
                        "10+"
                    } else {
                        getNumberAbbreveation(
                            pendingPostItemCount
                        )
                    }
                } ${
                    if (pendingPostItemCount == 1) {
                        "post"
                    } else {
                        "posts"
                    }
                }"
                val joinRequestText = "${
                    if (joinRequestItemCount > 10) {
                        "10+"
                    } else {
                        getNumberAbbreveation(
                            joinRequestItemCount
                        )
                    }
                } ${
                    if (joinRequestItemCount == 1) {
                        "join request"
                    } else {
                        "join requests"
                    }
                }"

                val postsNeedApprovalText =
                    "$postText ${if (pendingPostItemCount == 1) "requires" else "require"} approval"
                val yourPostsPendingText = "Your posts are pending for review"
                val joinRequestsText =
                    "$joinRequestText ${if (joinRequestItemCount == 1) "requires" else "require"} approval"

                if (showPendingPostsInfo && showJoinRequestsInfo) { // Both moderator-relevant items
                    desc = "$postText and $joinRequestText require approval"
                } else if (showPendingPostsInfo) { // Only Posts
                    desc = if (isModerator) postsNeedApprovalText else yourPostsPendingText
                } else if (showJoinRequestsInfo) { // Only Join Requests (implies isModerator)
                    desc = joinRequestsText
                }
                Pair(title, desc)
            }
        }

        val bannerTitle by remember { derivedStateOf { bannerContent.first } }
        val description by remember { derivedStateOf { bannerContent.second } }

        if (description.isNotEmptyOrBlank()) {
            Row(modifier = Modifier.padding(bottom = 12.dp, start = 16.dp, end = 16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(size = 4.dp)
                        )
                        .clickable {
                            behavior.goToPendingRequestPage(
                                AmityCommunityProfilePageBehavior.Context(
                                    pageContext = context,
                                    community = community
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(6.dp)
                                    .height(6.dp)
                                    .background(
                                        color = AmityTheme.colors.primary,
                                        shape = RoundedCornerShape(size = 3.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = bannerTitle,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight(600),
                                    color = AmityTheme.colors.base
                                )
                            )
                        }
                        Text(
                            text = description,
                            style = TextStyle(
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight(400),
                                color = AmityTheme.colors.baseShade1,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmityCommunityJoinButton(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
) {
    val behavior = remember {
        AmitySocialBehaviorHelper.communityProfilePageBehavior
    }
    var isPendingJoinRequest by remember { mutableStateOf<Boolean>(false) }
    var joinRequest by remember { mutableStateOf<AmityJoinRequest?>(null) }
    var isLoadingStatus by remember { mutableStateOf(true) }

    val compositeDisposable = remember { CompositeDisposable() }

    DisposableEffect(community) {
        if (AmityCoreClient.isSignedIn()) {
            community.getMyJoinRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ request ->
                    isPendingJoinRequest =
                        request != null && request.getStatus() == AmityJoinRequestStatus.PENDING
                    joinRequest =
                        if (request.getStatus() == AmityJoinRequestStatus.PENDING) request else null
                    isLoadingStatus = false
                }, { error ->
                    // Request failed or no join request exists
                    isPendingJoinRequest = false
                    isLoadingStatus = false
                })
                .apply {
                    compositeDisposable.add(this)
                }
        } else {
            // User is not signed in then loading should be successful immediately
            isLoadingStatus = false
            isPendingJoinRequest = false
        }

        onDispose {
            compositeDisposable.clear()
        }
    }

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = if (!community.isJoined() && isPendingJoinRequest) "community_cancel_request_button" else "community_join_button"
    ) {
        if (!community.isJoined() && !isLoadingStatus) {
            if (isPendingJoinRequest) {
                OutlinedButton(
                    onClick = {
                        if (AmityCoreClient.isVisitor()) {
                            behavior.handleVisitorUserAction()
                        } else {
                            joinRequest?.let { request ->
                                val disposable = request.cancel()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnComplete {
                                        isPendingJoinRequest = false
                                    }
                                    .doOnError {
                                        pageScope?.showSnackbar(
                                            message = "Failed to cancel your request. Please try again.",
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                    .subscribe()

                                compositeDisposable.add(disposable)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
                    enabled = true,
                    elevation = null,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(width = 1.dp, color = AmityTheme.colors.secondaryShade3),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                    ),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Icon(
                        painter = painterResource(id = getConfig().getIcon()),
                        contentDescription = "Join community icon",
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancel request",
                        style = AmityTheme.typography.bodyBold
                    )
                }
            } else {
                Row(modifier = modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                if (AmityCoreClient.isVisitor()) {
                                    behavior.handleVisitorUserAction()
                                } else {
                                    isLoadingStatus = true
                                    community.join()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { result ->
                                            isLoadingStatus = false
                                            when (result) {
                                                is AmityJoinResult.Success -> {
                                                    // Successfully joined the community
                                                    isPendingJoinRequest = false
                                                    pageScope?.showSnackbar(
                                                        message = "You joined ${community.getDisplayName()}.",
                                                        drawableRes = R.drawable.amity_ic_snack_bar_success,
                                                    )
                                                }

                                                is AmityJoinResult.Pending -> {
                                                    // Join request is pending
                                                    joinRequest = result.request
                                                    isPendingJoinRequest = true
                                                    pageScope?.showSnackbar(
                                                        message = "Requested to join. You will be notified once your request is accepted.",
                                                        drawableRes = R.drawable.amity_ic_snack_bar_success,
                                                    )
                                                }
                                            }
                                        }
                                        .doOnError {
                                            isLoadingStatus = false
                                            pageScope?.showSnackbar(
                                                message = "Failed to join the community. Please try again.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                        .subscribe()
                                        .apply {
                                            compositeDisposable.add(this)
                                        }
                                }
                            }
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                color = AmityTheme.colors.primary,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(start = 12.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = getConfig().getIcon()),
                                contentDescription = "Join community icon",
                                modifier = Modifier
                                    .size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Join",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight(600),
                                    color = Color.White,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
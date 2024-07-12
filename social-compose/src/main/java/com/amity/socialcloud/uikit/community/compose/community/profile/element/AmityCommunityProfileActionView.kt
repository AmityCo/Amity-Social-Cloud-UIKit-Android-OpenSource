package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@Composable
fun AmityCommunityProfileActionView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity,
) {
	AmityCommunityPendingPost(pageScope = pageScope, componentScope = componentScope, community = community)
	AmityCommunityJoinButton(pageScope = pageScope, componentScope = componentScope, community = community)
}

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
		val isModerator = AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY)
			.atCommunity(community.getCommunityId())
			.check()
			.asFlow()
			.collectAsState(initial = false)
		
		if(isModerator.value) {
			val pendingPostCount = community.getPostCount(AmityReviewStatus.UNDER_REVIEW).asFlow().collectAsState(initial = 0)
			if (pendingPostCount.value > 0) {
				Row(modifier = Modifier.padding(bottom = 12.dp, start = 16.dp, end = 16.dp)) {
					Box(modifier = Modifier
						.fillMaxWidth()
						.height(62.dp)
						.background(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 4.dp)
						)
						.clickable {
						   behavior.goToPostReviewPage(context, community.getCommunityId())
						},
						contentAlignment = Alignment.Center
					) {
						Column {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Box(modifier = Modifier
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
									text = "Pending posts",
									style = TextStyle(
										fontSize = 15.sp,
										lineHeight = 20.sp,
										fontWeight = FontWeight(600),
										color = AmityTheme.colors.base
									)
								)
							}
							Text(
								text = "${getNumberAbbreveation(pendingPostCount.value)} ${ if (pendingPostCount.value == 1) "post" else "posts"} need approval",
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
}

@Composable
fun AmityCommunityJoinButton(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "community_join_button"
	) {
		if (!community.isJoined()) {
			Row(modifier = Modifier.padding(16.dp)) {
				Box(modifier = Modifier
					.clickable {
						AmitySocialClient
							.newCommunityRepository()
							.joinCommunity(community.getCommunityId())
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.doOnError {  }
							.subscribe()
					}
					.fillMaxWidth()
					.height(40.dp)
					.background(color = AmityTheme.colors.primary, shape = RoundedCornerShape(size = 8.dp))
					.padding(start = 12.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
					contentAlignment = Alignment.Center
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Text(
							text = "+",
							style = TextStyle(
								fontSize = 15.sp,
								lineHeight = 20.sp,
								fontWeight = FontWeight(400),
								color = Color.White,
							),
							modifier = Modifier.padding(end = 8.dp)
						)
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
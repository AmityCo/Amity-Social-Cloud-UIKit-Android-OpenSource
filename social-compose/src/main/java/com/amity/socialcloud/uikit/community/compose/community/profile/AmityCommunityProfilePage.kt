import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getActivity
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileShimmer
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileTabView
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfileViewModel
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityEmptyView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.CommunityProfileTab
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostPriority
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCommunityCreatePostMenuComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCreatePostMenuComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityPostAdView
import com.amity.socialcloud.uikit.community.compose.socialhome.components.CommunityCreatePostTargetType
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraRelatedButtonElement

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AmityCommunityProfilePage(
	communityId: String,
) {
	AmityBasePage(pageId = "community_profile") {
		val viewModel = remember { AmityCommunityProfileViewModel(communityId) }
		val context = LocalContext.current
		val lazyListState = rememberLazyListState()
		val state by remember { viewModel.communityProfileState }.collectAsState()
		val community by remember { derivedStateOf { state.community } }
		var expanded by remember { mutableStateOf(false) }
		
		var isHeaderSticky by remember { mutableStateOf(false) }
		val pullRefreshState = rememberPullRefreshState(
			refreshing = state.isRefreshing,
			onRefresh = {
				viewModel.refresh()
			}
		)
		
		val behavior = remember {
			AmitySocialBehaviorHelper.communityProfilePageBehavior
		}
		val announcement = remember(state.isRefreshing) { viewModel.getAnnouncement() }.collectAsLazyPagingItems()
		val posts = remember(state.isRefreshing) { viewModel.getCommunityPosts() }.collectAsLazyPagingItems()
		
		var selectedTabIndex by remember { mutableStateOf(0) }
		val tabs = listOf(
			CommunityProfileTab("community_feed", R.drawable.amity_ic_community_feed),
//		CommunityProfileTab("community_pin", R.drawable.amity_ic_community_pin),
		)
		
		Box(
			modifier = Modifier
				.fillMaxSize()
				.pullRefresh(pullRefreshState)
		) {
// TODO: 12/7/24 Temporary disable sticky header to align with iOS
//			LaunchedEffect(lazyListState) {
//				snapshotFlow { lazyListState.firstVisibleItemIndex }
//					.collect { index ->
//						if (index > 1 && !isHeaderSticky) {
//							isHeaderSticky = true
//						} else if (index <= 1 && isHeaderSticky) {
//							isHeaderSticky = false
//
//						}
//					}
//			}
			LazyColumn(
				state = lazyListState
			) {
				if (community == null || state.isRefreshing) {
					item {
						AmityCommunityProfileShimmer()
					}
				} else {
					stickyHeader {
						if (isHeaderSticky) {
							TopAppBar(
								title = {
									Text(
										community!!.getDisplayName(),
										maxLines = 1,
										overflow = TextOverflow.Ellipsis,
										style = TextStyle(
											fontSize = 17.sp,
											lineHeight = 24.sp,
											fontWeight = FontWeight(600),
											color = AmityTheme.colors.base,
										)
									)
								},
								navigationIcon = {
									AmityStoryCameraRelatedButtonElement(
										modifier = Modifier
											.padding(start = 16.dp, end = 12.dp)
											.size(32.dp),
										icon = R.drawable.amity_ic_social_back,
										onClick = {
											context.getActivity()?.finish()
										}
									)
								},
								actions = {
									AmityStoryCameraRelatedButtonElement(
										modifier = Modifier
											.padding(start = 12.dp, end = 16.dp)
											.size(32.dp),
										icon = R.drawable.amity_ic_more_horiz,
										onClick = {
											behavior.goToCommunitySettingPage(
												context = context,
												communityId = communityId,
											)
										}
									)
								}
							)
						}
					}
					item {
						AmityCommunityHeaderComponent(community = community!!)
					}
				}
				item {
					AmityCommunityProfileTabView(tabs = tabs, selectedIndex = selectedTabIndex) { index ->
						selectedTabIndex = index
					}
				}
				if (announcement.itemCount > 0) {
					items(
						count = announcement.itemCount,
						key = {
							"announcement_${announcement[it]?.post?.getPostId() ?: it}"
						}
					) { index ->
						announcement[index]?.post?.let { post ->
							// TODO: 3/6/24 currently only support text, image, and video post
							when (post.getData()) {
								is AmityPost.Data.TEXT,
								is AmityPost.Data.IMAGE,
								is AmityPost.Data.VIDEO -> {
								}
								
								else -> return@items
							}
							
							AmityPostContentComponent(
								post = post,
								style = AmityPostContentComponentStyle.ANNOUNCEMENT_FEED,
								hideMenuButton = false,
								hideTarget = true,
							) {
								behavior.goToPostDetailPage(
									context = context,
									postId = post.getPostId(),
									priority = AmityPostPriority.ANNOUNCEMENT
								)
							}
							AmityNewsFeedDivider()
						}
					}
				}
				items(
					count = posts.itemCount,
					key = {
						(posts[it] as? AmityListItem.PostItem)?.post?.getPostId() ?: it
					}
				) { index ->
					when (val data = posts[index]) {
						is AmityListItem.PostItem -> {
							val post = data.post
							val isAnnouncement = announcement.itemSnapshotList.items
								.map { it.postId }
								.contains(post.getPostId())
							
							if (isAnnouncement) {
								return@items
							}
							
							// TODO: 3/6/24 currently only support text, image, and video post
							when (post.getData()) {
								is AmityPost.Data.TEXT,
								is AmityPost.Data.IMAGE,
								is AmityPost.Data.VIDEO -> {
								}
								
								else -> return@items
							}
							
							AmityPostContentComponent(
								post = post,
								style = AmityPostContentComponentStyle.FEED,
								hideMenuButton = false,
								hideTarget = true,
							) {
								behavior.goToPostDetailPage(
									context = context,
									postId = post.getPostId(),
								)
							}
							AmityNewsFeedDivider()
						}
						
						is AmityListItem.AdItem -> {
							val ad = data.ad
							
							AmityPostAdView(
								ad = ad,
							)
							AmityNewsFeedDivider()
						}
						
						else -> {}
					}
				}
				if (announcement.itemCount == 0 && posts.itemCount == 0) {
					item {
						Box(
							modifier = Modifier
								.height(355.dp),
						) {
							AmityCommunityEmptyView()
						}
					}
				}
			}
			FloatingActionButton(
				onClick = {
					expanded = true
				},
				shape = RoundedCornerShape(size = 25.dp),
				containerColor = AmityTheme.colors.primary,
				modifier = Modifier
					.padding(16.dp)
					.size(50.dp)
					.align(Alignment.BottomEnd)
			) {
				Icon(
					painter = painterResource(id = R.drawable.amity_ic_plus), // Replace with your icon resource
					contentDescription = "create post",
					tint = Color.White,
					modifier = Modifier.size(25.dp)
				)
			}
			if (community != null) {
				AmityCommunityCreatePostMenuComponent(
					modifier = Modifier,
					expanded = expanded,
					community = community!!,
					onDismiss = { expanded = false },
				)
			}
		}
	}
	
}
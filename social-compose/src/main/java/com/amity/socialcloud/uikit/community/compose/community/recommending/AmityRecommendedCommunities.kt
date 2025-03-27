package com.amity.socialcloud.uikit.community.compose.community.recommending

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityJoinButton
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityCommunityCategoryView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityRecommendedCommunityShimmer


@Composable
fun AmityRecommendedCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onStateChanged: (AmityRecommendedCommunitiesViewModel.CommunityListState) -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.exploreComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityRecommendedCommunitiesViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // Remember the Flow of communities
    val communitiesFlow = remember {
        viewModel.getRecommendedCommunities()
    }

    val communities = communitiesFlow.collectAsState(initial = emptyList())
    val communityListState by viewModel.communityListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "recommended_communities"
    ) {
        onStateChanged(communityListState)
        when (communityListState) {
            AmityRecommendedCommunitiesViewModel.CommunityListState.SUCCESS -> {
                Column(
                    modifier = modifier
                        .height(263.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Recommended for you",
                        style = AmityTheme.typography.titleLegacy,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    ) {
                        items(
                            count = communities.value.size,
                            key = { index -> index }
                        ) { index ->
                            val community = communities.value[index]

                            AmityRecommendedCommunityView(
                                modifier = modifier,
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                community = community,
                                onClick = {
                                    behavior.goToCommunityProfilePage(
                                        context = context,
                                        communityId = community.getCommunityId(),
                                    )
                                },
                            )
                        }
                    }
                }
            }

            AmityRecommendedCommunitiesViewModel.CommunityListState.LOADING -> {
                AmityRecommendedCommunityShimmer()
            }

            AmityRecommendedCommunitiesViewModel.CommunityListState.EMPTY -> {

            }

            AmityRecommendedCommunitiesViewModel.CommunityListState.ERROR -> {

            }
        }
    }
}

@Composable
fun AmityRecommendedCommunityAvatarView(
    modifier: Modifier = Modifier,
    image: AmityImage?,
    placeholder: Int = R.drawable.amity_ic_community_placeholder,
) {
    val url = image?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Box(
        modifier = modifier
            .width(268.dp)
            .height(125.dp)
    ) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )

        )
        if (painterState !is AsyncImagePainter.State.Success) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(AmityTheme.colors.baseShade3)
            ) {
                Icon(
                    painter = painterResource(id = placeholder),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                )
            }
        }
    }
}

@Composable
fun AmityRecommendedCommunityView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    onClick: (AmityCommunity) -> Unit,
) {
    Column(
        modifier = modifier
            .width(268.dp)
            .clickableWithoutRipple {
                onClick(community)
            }
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "community_avatar"
        ) {
            AmityRecommendedCommunityAvatarView(
                image = community.getAvatar(),
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .height(64.dp)
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .padding(10.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!community.isPublic()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_private_badge"
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            tint = AmityTheme.colors.baseShade1,
                            contentDescription = "Private Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = componentScope,
                    elementId = "community_display_name"
                ) {
                    Text(
                        text = community.getDisplayName().trim(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }

                if (community.isOfficial()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_official_badge"
                    ) {
                        Image(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = "Verified Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (community.getCategories().isNotEmpty()) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            elementId = "community_category_name"
                        ) {
                            AmityCommunityCategoryView(
                                categories = community.getCategories(),
                                modifier = modifier
                                    .padding(top = 6.dp, end = 4.dp)
                                    .testTag(getAccessibilityId()),
                                maxPreview = 2
                            )
                        }
                    }
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_members_count"
                    ) {
                        Text(
                            text = "${community.getMemberCount().readableNumber()} members",
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade1,
                            ),
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }

                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    AmityCommunityJoinButton(
                        community = community
                    )
                }
            }
        }
    }
}
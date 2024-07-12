package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getActivity
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraRelatedButtonElement

@Composable
fun AmityCommunityCoverView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "community_cover"
	) {
		val behavior = remember {
			AmitySocialBehaviorHelper.communityProfilePageBehavior
		}
		val context = LocalContext.current
		val imageUrl = community.getAvatar()?.getUrl(AmityImage.Size.LARGE)
		Box(
			modifier = modifier
				.fillMaxWidth()
				.height(188.dp)
				.background(
					brush = Brush.verticalGradient(
						colors = listOf(
							AmityTheme.colors.baseShade3,
							AmityTheme.colors.baseShade2,
						)
					)
				)
		) {
			if (imageUrl != null) {
				Box(modifier = Modifier.matchParentSize()) {
					AsyncImage(
						model = ImageRequest
							.Builder(LocalContext.current)
							.data(imageUrl)
							.crossfade(true)
							.networkCachePolicy(CachePolicy.ENABLED)
							.diskCachePolicy(CachePolicy.ENABLED)
							.memoryCachePolicy(CachePolicy.ENABLED)
							.build(),
						modifier = Modifier
							.fillMaxWidth(),
						contentDescription = "Community cover image",
						contentScale = ContentScale.FillWidth,
					)
				}
			}
			Row(
				Modifier
					.matchParentSize()
					.padding(top = 60.dp, start = 16.dp, end = 16.dp)
			) {
				AmityStoryCameraRelatedButtonElement(
					modifier = Modifier
						.size(32.dp)
						.testTag(getAccessibilityId()),
					icon = R.drawable.amity_ic_social_back,
					onClick = {
						context.getActivity()?.finish()
					}
				)
				Spacer(modifier = Modifier.weight(1f))
				AmityStoryCameraRelatedButtonElement(
					modifier = Modifier
						.size(32.dp)
						.testTag(getAccessibilityId()),
					icon = R.drawable.amity_ic_more_horiz,
					onClick = {
						behavior.goToCommunitySettingPage(
							context = context,
							communityId = community.getCommunityId(),
						)
					}
				)
			}
		}
	}
}
package com.amity.socialcloud.uikit.community.compose.community.profile.element

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
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
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderStyle
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraRelatedButtonElement
import com.amity.socialcloud.uikit.community.compose.utils.BlurImage
import com.amity.socialcloud.uikit.community.compose.utils.LegacyBlurImage
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityCommunityCoverView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity,
	style: AmityCommunityHeaderStyle = AmityCommunityHeaderStyle.EXPANDED,
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
		val coverUrl by remember { derivedStateOf { community?.getAvatar()?.getUrl(AmityImage.Size.LARGE) } }
		val painter = rememberAsyncImagePainter(
			contentScale = ContentScale.FillWidth,
			model = ImageRequest.Builder(LocalContext.current)
				.data(coverUrl)
				.allowHardware(false)
				.dispatcher(Dispatchers.IO)
				.diskCachePolicy(CachePolicy.ENABLED)
				.memoryCachePolicy(CachePolicy.ENABLED)
				.build(),
		)
		var bitmap: Bitmap? by remember {
			mutableStateOf(null)
		}
		val paintState = painter.state
		if (paintState is AsyncImagePainter.State.Success) {
			val drawable = paintState.result.drawable
			bitmap = drawable.toBitmap()
		}
		if (style == AmityCommunityHeaderStyle.EXPANDED) {
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
				if (coverUrl != null) {
					Box(modifier = Modifier.matchParentSize()) {
						Image(
							painter = painter,
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
					AmityBaseElement(
						pageScope = pageScope,
						componentScope = componentScope,
						elementId = "back_button"
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
					}
					Spacer(modifier = Modifier.weight(1f))
					AmityBaseElement(
						pageScope = pageScope,
						componentScope = componentScope,
						elementId = "menu_button"
					) {
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
		} else {
			Box {
				if (coverUrl == null) {
					Box(
						modifier = modifier
							.matchParentSize()
							.background(
								brush = Brush.verticalGradient(
									colors = listOf(
										AmityTheme.colors.baseShade3,
										AmityTheme.colors.baseShade2,
									)
								)
							)
					)
				} else {
					Image(
						painter = painter,
						contentDescription = "Community profile cover",
						modifier = Modifier
							.matchParentSize()
							.align(Alignment.Center)
							.alpha(0.5f)
					)
					if (bitmap != null) {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
							LegacyBlurImage(bitmap!!, 25f, modifier = Modifier
								.matchParentSize()
								.align(Alignment.Center))
						} else {
							BlurImage(
								bitmap!!,
								Modifier
									.matchParentSize()
									.align(Alignment.Center)
									.blur(radiusX = 15.dp, radiusY = 15.dp)
							)
						}
					}
				}
				Column(modifier = Modifier
					.fillMaxWidth()
					.background(Color.Transparent)
				) {
					Row(
						verticalAlignment = Alignment.Bottom,
						modifier = Modifier
							.fillMaxWidth()
							.height(102.dp)
							.padding(16.dp)
					) {
						AmityStoryCameraRelatedButtonElement(
							modifier = Modifier
								.padding(end = 12.dp)
								.size(32.dp),
							icon = R.drawable.amity_ic_social_back,
							onClick = {
								context.getActivity()?.finish()
							}
						)
						Text(
							community!!.getDisplayName(),
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							style = TextStyle(
								fontSize = 17.sp,
								lineHeight = 24.sp,
								fontWeight = FontWeight(600),
								color = Color.White,
							),
							modifier = Modifier
								.weight(1f)
								.padding(bottom = 4.dp)
						)
						AmityStoryCameraRelatedButtonElement(
							modifier = Modifier
								.padding(start = 12.dp)
								.size(32.dp),
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
	}
}
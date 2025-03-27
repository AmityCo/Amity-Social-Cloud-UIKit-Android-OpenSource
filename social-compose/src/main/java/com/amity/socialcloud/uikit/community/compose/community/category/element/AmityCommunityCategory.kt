package com.amity.socialcloud.uikit.community.compose.community.category.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmityCommunityCategoryList(
    modifier: Modifier = Modifier,
    categories: List<AmityCommunityCategory>,
    onRemove: (AmityCommunityCategory) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        categories.forEach { category ->
            AmityCommunityCategoryElement(
                category = category,
                onRemove = onRemove,
            )
        }
    }
}

@Composable
fun AmityCommunityCategoryElement(
    modifier: Modifier = Modifier,
    category: AmityCommunityCategory,
    onRemove: ((AmityCommunityCategory) -> Unit)?,
    onClick: ((AmityCommunityCategory) -> Unit)? = null,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(category.getAvatar()?.getUrl())
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .widthIn(max = 600.dp)
            .background(color = Color.Transparent)
            .border(
                border = BorderStroke(1.dp, AmityTheme.colors.baseShade4),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(4.dp)
            .clickableWithoutRipple {
                if (onClick != null) {
                    onClick(category)
                }
            }
    ) {
        Box() {
            Image(
                painter = painter,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.baseShade4)
            )
            if (painterState !is AsyncImagePainter.State.Success) {
                Image(
                    painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_category_placeholder),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
            }
        }
        Spacer(modifier.width(8.dp))
        Text(
            text = category.getName().trim(),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, false)
        )
        Spacer(modifier.width(4.dp))
        if (onRemove != null) {
            Icon(
                painter = painterResource(R.drawable.amity_ic_close2),
                contentDescription = "Close",
                tint = AmityTheme.colors.baseShade1,
                modifier = modifier
                    .size(24.dp)
                    .padding(6.dp)
                    .clickableWithoutRipple { onRemove(category) }
            )
            Spacer(modifier.width(4.dp))
        }
    }
}
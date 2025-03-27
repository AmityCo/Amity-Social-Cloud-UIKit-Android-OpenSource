package com.amity.socialcloud.uikit.community.compose.community.membership.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmityCommunityAddMemberList(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    users: List<AmityUser>,
    onAddAction: () -> Unit,
    onRemoveAction: (AmityUser) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        users.forEach { user ->
            AmityCommunityAddMemberElement(
                user = user,
                onRemoveAction = onRemoveAction,
            )
        }
        AmityCommunityAddMemberButton(
            pageScope = pageScope,
            onClick = onAddAction,
        )
    }
}

@Composable
fun AmityCommunityAddMemberRowList(
    modifier: Modifier = Modifier,
    users: List<AmityUser>,
    onRemoveAction: (AmityUser) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = users.size,
            key = { index -> users[index].getUserId() }
        ) { index ->
            val user = users[index]

            AmityCommunityAddMemberElement(
                user = user,
                onRemoveAction = onRemoveAction,
            )
        }
    }
}

@Composable
fun AmityCommunityAddMemberElement(
    modifier: Modifier = Modifier,
    user: AmityUser,
    onRemoveAction: (AmityUser) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = Color.Transparent)
            .width(64.dp)
    ) {
        Box {
            AmityUserAvatarView(
                user = user,
                size = 40.dp,
                modifier = modifier,
            )

            Box(
                modifier = modifier
                    .offset(x = 2.dp)
                    .clip(CircleShape)
                    .background(Color(0).copy(alpha = 0.3f))
                    .align(Alignment.TopEnd)
                    .size(16.dp)
                    .padding(5.dp)
                    .clickableWithoutRipple { onRemoveAction(user) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_close2),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(modifier.height(4.dp))
        Text(
            text = user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy,
            modifier = modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}


@Composable
fun AmityCommunityAddMemberButton(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onClick: () -> Unit,
) {
    AmityBaseElement(
        pageScope = pageScope,
        elementId = "community_add_member_button"
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(color = Color.Transparent)
                .width(64.dp)
                .clickableWithoutRipple { onClick() }
        ) {
            Box {
                Image(
                    painter = painterResource(getConfig().getIcon()),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AmityTheme.colors.baseShade4)
                        .padding(6.dp)
                )
            }
            Spacer(modifier.height(4.dp))
            Text(
                text = getConfig().getText(),
                style = AmityTheme.typography.bodyLegacy,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun AmityCommunityAddMemberItem(
    modifier: Modifier = Modifier,
    viewModel: AmityCommunityAddMemberPageViewModel,
    user: AmityUser,
    isEnabled: Boolean = true,
    onSelect: (AmityUser) -> Unit,
    onRemove: (AmityUser) -> Unit,
) {
    val selectedItemStates by viewModel.selectedItemStates.collectAsState()
    val isSelected by remember(user.getUserId()) {
        derivedStateOf {
            selectedItemStates.find {
                it.userId == user.getUserId()
            }?.isSelected == true
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickableWithoutRipple {
                if (!isEnabled && !isSelected) return@clickableWithoutRipple

                if (isSelected) {
                    onRemove(user)
                } else {
                    onSelect(user)
                }

                viewModel.updateSelectedItemState(
                    userId = user.getUserId(),
                    isSelected = !isSelected,
                )
            }
    ) {
        AmityUserAvatarView(
            user = user,
            size = 40.dp,
            modifier = modifier,
        )

        Spacer(modifier.width(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = user.getDisplayName() ?: user.getUserId(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = Modifier.weight(1f, fill = false)
            )
            val isBrandUser = user.isBrand()
            if (isBrandUser) {
                val badge = R.drawable.amity_ic_brand_badge
                Image(
                    painter = painterResource(id = badge),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp)
                        .testTag("user_view/brand_user_icon")
                )
            }
        }
        Spacer(modifier.width(8.dp))
        Image(
            painter = painterResource(
                if (isSelected) R.drawable.amity_ic_category_selected
                else R.drawable.amity_ic_category_not_selected
            ),
            contentDescription = "Selection",
            modifier = modifier.size(24.dp)
        )
    }
}
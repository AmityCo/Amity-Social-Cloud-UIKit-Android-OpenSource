package com.amity.socialcloud.uikit.community.compose.ui.components.feed.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmityCommunityPrivateView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "empty_feed"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_private_community),
                contentDescription = "empty feed icon",
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(top = 7.5.dp, bottom = 7.5.dp),
                tint = AmityTheme.colors.baseShade4
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This community is private",
                style = AmityTheme.typography.titleBold,
                color = AmityTheme.colors.baseShade3,
                fontSize = 17.sp,
            )
            Text(
                text = "Join this community to see its content and members.",
                style = AmityTheme.typography.caption,
                color = AmityTheme.colors.baseShade3,
                fontSize = 13.sp,
            )
        }
    }
}
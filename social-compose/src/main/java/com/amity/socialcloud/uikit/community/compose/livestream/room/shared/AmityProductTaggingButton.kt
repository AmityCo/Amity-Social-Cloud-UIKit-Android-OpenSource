package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityProductTaggingButton(
    modifier: Modifier = Modifier,
    taggedProductsCount: Int,
    onClick: () -> Unit,
    componentScope: AmityComposeComponentScope? = null,
) {
    AmityBaseElement(
        componentScope = componentScope,
        elementId = "product_tagging_button_element"
    ) {
        Box(modifier.size(50.dp)){
            Image(
                painter = painterResource(R.drawable.amity_ic_room_product_tags),
                contentDescription = "open tagged products bottomsheet button",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
                    .testTag("product_tagging_button_element")
                    .clickableWithoutRipple(onClick = onClick),
            )
            if (taggedProductsCount > 0) {
                Text(
                    text = "$taggedProductsCount",
                    style = AmityTheme.typography.body.copy(color = Color.White),
                    modifier = Modifier
                        .background(AmityTheme.colors.alert, RoundedCornerShape(20.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}
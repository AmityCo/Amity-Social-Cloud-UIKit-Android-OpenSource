package com.amity.socialcloud.uikit.community.compose.livestream.errorhandling

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType

@Composable
fun AmityLivestreamTerminatedPage(
    livestreamScreenType: LivestreamScreenType?,
) {

    val context = LocalContext.current

    val mainIcon = painterResource(
        id = if (livestreamScreenType == LivestreamScreenType.CREATE) {
            R.drawable.amity_ic_livestream_terminate
        } else {
            R.drawable.amity_ic_live_stream_terminated_watch_part
        }
    )
    val mainTitle = if (livestreamScreenType == LivestreamScreenType.CREATE) {
        stringResource(R.string.amity_v4_create_livestream_terminated_title)
    } else {
        stringResource(R.string.amity_v4_create_livestream_terminated_watch_part_title)
    }
    val mainDesc = if (livestreamScreenType == LivestreamScreenType.CREATE) {
        stringResource(R.string.amity_v4_create_livestream_terminated_desc)
    } else {
        stringResource(R.string.amity_v4_create_livestream_terminated_watch_part_desc)
    }

    AmityBasePage(pageId = "livestream_terminated_page") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .systemBarsPadding()
                .background(AmityTheme.colors.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 17.dp),
                    text = stringResource(R.string.amity_v4_create_livestream_terminated_toolbar_title),
                    style = AmityTheme.typography.titleLegacy.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = AmityTheme.colors.base
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = AmityTheme.colors.baseShade4
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(56.dp),
                    painter = mainIcon,
                    tint = AmityTheme.colors.base,
                    contentDescription = "terminated icon"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = mainTitle,
                    style = AmityTheme.typography.headLine,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = mainDesc,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        textAlign = TextAlign.Center,
                        color = AmityTheme.colors.baseShade1
                    ),
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = AmityTheme.colors.baseShade4
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.amity_v4_create_livestream_terminated_question),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = AmityTheme.colors.base
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (livestreamScreenType == LivestreamScreenType.CREATE) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.amity_ic_livestream_terminated_stop),
                            tint = AmityTheme.colors.base,
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.amity_v4_create_livestream_terminated_first_reason),
                            style = AmityTheme.typography.bodyLegacy
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.amity_v4_ic_trash),
                        tint = AmityTheme.colors.base,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.amity_v4_create_livestream_terminated_second_reason),
                        style = AmityTheme.typography.bodyLegacy
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = AmityTheme.colors.baseShade4
            )

            AmityBaseElement(
                pageScope = getPageScope(),
                elementId = "livestream_terminated_action_button"
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.primary,
                    ),
                    onClick = {
                        context.closePageWithResult(Activity.RESULT_OK)
                    }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
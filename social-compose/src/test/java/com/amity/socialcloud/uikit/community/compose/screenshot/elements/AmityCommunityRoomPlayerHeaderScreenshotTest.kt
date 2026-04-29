package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.model.core.role.AmityRoles
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.view.CommunityRoomPlayerHeader
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AmityCommunityRoomPlayerHeaderScreenshotTest : BaseScreenshotTest() {

    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(
            resultValidator = { result ->
                result.pixelDifferences / result.pixelCount.toFloat() < 0.001f
            }
        )
    )

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun community_room_player_header_with_badges() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "community_room_player_page") {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityAvatarView(
                            image = null,
                            size = 32.dp,
                            iconPadding = 24.dp,
                            placeholder = R.drawable.amity_ic_community_placeholder,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f, fill = false),
                                    text = "Test Community",
                                    style = AmityTheme.typography.body.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    elementId = "community_official_badge"
                                ) {
                                    Image(
                                        painter = painterResource(id = getConfig().getIcon()),
                                        contentDescription = "Verified Community",
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f, fill = false),
                                    text = "By Creator Name",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                    contentDescription = "Brand badge",
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/community_room_player_header_with_badges.png",
                roborazziOptions = screenshotOptions,
            )
    }

    @Test
    fun community_room_player_header_without_badges() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "community_room_player_page") {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityAvatarView(
                            image = null,
                            size = 32.dp,
                            iconPadding = 24.dp,
                            placeholder = R.drawable.amity_ic_community_placeholder,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f, fill = false),
                                    text = "Test Community",
                                    style = AmityTheme.typography.body.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f, fill = false),
                                    text = "By Creator Name",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/community_room_player_header_without_badges.png",
                roborazziOptions = screenshotOptions,
            )
    }

    @Test
    fun community_room_player_header_private_community() {
        val community = mockk<AmityCommunity>(relaxed = true) {
            every { isPublic() } returns false
            every { isOfficial() } returns false
            every { getDisplayName() } returns "Test Community"
            every { getAvatar() } returns null
        }
        val communityTarget = mockk<AmityPost.Target.COMMUNITY>(relaxed = true) {
            every { getCommunity() } returns community
        }
        val creator = mockk<com.amity.socialcloud.sdk.model.core.user.AmityUser>(relaxed = true) {
            every { getDisplayName() } returns "Creator Name"
            every { getRoles() } returns AmityRoles(emptyList())
            every { getAvatar() } returns null
        }
        val post = mockk<AmityPost>(relaxed = true) {
            every { getTarget() } returns communityTarget
            every { getCreator() } returns creator
        }
        val room = mockk<AmityRoom>(relaxed = true) {
            every { getPost() } returns post
        }

        composeTestRule.setContent {
            AmityBasePage(pageId = "community_room_player_page") {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    CommunityRoomPlayerHeader(
                        pageScope = getPageScope(),
                        room = room,
                        onCloseClick = {},
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/community_room_player_header_private_community.png",
                roborazziOptions = screenshotOptions,
            )
    }

    @Test
    fun community_room_player_header_with_official_badge() {
        val community = mockk<AmityCommunity>(relaxed = true) {
            every { isPublic() } returns false
            every { isOfficial() } returns true
            every { getDisplayName() } returns "Test Community"
            every { getAvatar() } returns null
        }
        val communityTarget = mockk<AmityPost.Target.COMMUNITY>(relaxed = true) {
            every { getCommunity() } returns community
        }
        val creator = mockk<com.amity.socialcloud.sdk.model.core.user.AmityUser>(relaxed = true) {
            every { getDisplayName() } returns "Creator Name"
            every { getRoles() } returns AmityRoles(listOf("brand-partner"))
            every { getAvatar() } returns null
        }
        val post = mockk<AmityPost>(relaxed = true) {
            every { getTarget() } returns communityTarget
            every { getCreator() } returns creator
        }
        val room = mockk<AmityRoom>(relaxed = true) {
            every { getPost() } returns post
        }

        composeTestRule.setContent {
            AmityBasePage(pageId = "community_room_player_page") {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    CommunityRoomPlayerHeader(
                        pageScope = getPageScope(),
                        room = room,
                        onCloseClick = {},
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/community_room_player_header_with_official_badge.png",
                roborazziOptions = screenshotOptions,
            )
    }
}

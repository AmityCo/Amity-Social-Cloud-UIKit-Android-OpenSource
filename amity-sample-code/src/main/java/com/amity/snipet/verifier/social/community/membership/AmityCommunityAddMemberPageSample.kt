package com.amity.snipet.verifier.social.community.membership

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPage

class AmityCommunityAddMemberPageSample {
    /* begin_sample_code
    gist_id: 7fab62279114a63de099e36a387dbad3
    filename: AmityCommunityAddMemberPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Add Member Page sample
    */
    @Composable
    fun composeCommunityAddMemberPage(users: List<AmityUser>) {
        AmityCommunityAddMemberPage(
            users = users,
            onAddedAction = {

            }
        )
    }
    /* end_sample_code */
}
package com.amity.socialcloud.uikit.community.compose.community.pending.pending_invite

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.post.AmityCommunityPostPermissionPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.post.AmityCommunityPostPermissionPageActivity.Companion
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunityPendingInviteMemberPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val community = intent.getCommunity(EXTRA_PARAM_COMMUNITY) ?: return

        setContent {
            AmityCommunityPendingInviteMemberPage(
                community = community,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_COMMUNITY = "community"

        fun newIntent(
            context: Context,
            community: AmityCommunity,
        ): Intent {
            return Intent(
                context,
                AmityCommunityPendingInviteMemberPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
        }
    }
}
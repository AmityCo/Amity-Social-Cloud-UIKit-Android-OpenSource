package com.amity.socialcloud.uikit.community.compose.community.membership.invite

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getCommunity
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageActivity.Companion
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunityInviteMemberPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val users = getUsers(intent)
        val community = intent.getCommunity(EXTRA_PARAM_COMMUNITY)

        setContent {
            AmityCommunityInviteMemberPage(
                users = users,
            ) { users ->
                if (community != null) {
                    val successMessage = getString(R.string.amity_v4_community_invitation_create_success)
                    val failMessage = getString(R.string.amity_v4_community_invitation_create_failed)
                    community.createInvitations(users.map { it.getUserId() })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            AmityUIKitSnackbar.publishSnackbarMessage(
                                message = successMessage
                            )
                            closePage()
                        }
                        .doOnError {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                message = failMessage
                            )
                        }
                        .subscribe()
                } else {
                    val intent = Intent()
                    intent.putExtra(EXTRA_PARAM_USERS, users.toTypedArray())
                    closePageWithResult(RESULT_OK, intent)
                }
            }
        }
    }

    companion object {
        const val EXTRA_PARAM_COMMUNITY = "community"
        const val EXTRA_PARAM_USERS = "users"

        fun newIntent(
            context: Context,
            users: List<AmityUser>,
        ): Intent {
            return Intent(
                context,
                AmityCommunityInviteMemberPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_USERS, users.toTypedArray())
            }
        }

        fun newIntentWithCommunity(
            context: Context,
            community: AmityCommunity,
        ): Intent {
            return Intent(
                context,
                AmityCommunityInviteMemberPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
        }

        fun getUsers(intent: Intent): List<AmityUser> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(
                    EXTRA_PARAM_USERS,
                    AmityUser::class.java
                )?.toList() ?: emptyList()
            } else {
                intent.getParcelableArrayExtra(EXTRA_PARAM_USERS)
                    ?.mapNotNull { it as? AmityUser }
                    ?.toList() ?: emptyList()
            }
        }
    }
}
package com.amity.socialcloud.uikit.community.compose.community.membership.add

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.utils.closePageWithResult

class AmityCommunityAddMemberPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val users = getUsers(intent)

        setContent {
            AmityCommunityAddMemberPage(
                users = users,
            ) {
                val intent = Intent()
                intent.putExtra(EXTRA_PARAM_USERS, it.toTypedArray())
                closePageWithResult(RESULT_OK, intent)
            }
        }
    }

    companion object {
        const val EXTRA_PARAM_USERS = "users"

        fun newIntent(
            context: Context,
            users: List<AmityUser>,
        ): Intent {
            return Intent(
                context,
                AmityCommunityAddMemberPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_USERS, users.toTypedArray())
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
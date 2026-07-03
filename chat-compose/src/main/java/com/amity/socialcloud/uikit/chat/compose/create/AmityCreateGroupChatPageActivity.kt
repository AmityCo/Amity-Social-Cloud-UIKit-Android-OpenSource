package com.amity.socialcloud.uikit.chat.compose.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.core.user.AmityUser

class AmityCreateGroupChatPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var currentStep by remember { mutableStateOf(CreateGroupStep.SELECT_MEMBERS) }
            var selectedUsers by remember { mutableStateOf<List<AmityUser>>(emptyList()) }

            val modifier = Modifier
                .statusBarsPadding()
                .systemBarsPadding()

            when (currentStep) {
                CreateGroupStep.SELECT_MEMBERS, CreateGroupStep.EDIT_MEMBERS -> {
                    AmitySelectGroupMemberPage(
                        modifier = modifier,
                        onNext = { users ->
                            selectedUsers = users
                            currentStep = CreateGroupStep.CREATE_GROUP
                        },
                        onBack = {
                            if (currentStep == CreateGroupStep.SELECT_MEMBERS) {
                                finish()
                            } else {
                                currentStep = CreateGroupStep.CREATE_GROUP
                            }
                        }
                    )
                }
                CreateGroupStep.CREATE_GROUP -> {
                    AmityCreateGroupChatPage(
                        modifier = modifier,
                        selectedUsers = selectedUsers,
                        onBack = { backToHome ->
                            if (backToHome) {
                                finish()
                            } else {
                                currentStep = CreateGroupStep.SELECT_MEMBERS
                            }
                        },
                        onAddMember = {
                            currentStep = CreateGroupStep.EDIT_MEMBERS
                        }
                    )
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AmityCreateGroupChatPageActivity::class.java)
        }
    }
}

private enum class CreateGroupStep {
    SELECT_MEMBERS,
    CREATE_GROUP,
    EDIT_MEMBERS
}

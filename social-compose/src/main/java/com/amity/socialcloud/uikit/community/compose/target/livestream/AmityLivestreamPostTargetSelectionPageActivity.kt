package com.amity.socialcloud.uikit.community.compose.target.livestream

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmityLivestreamPostTargetSelectionPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isEdit = intent.getBooleanExtra(EXTRA_PARAM_IS_EDIT_MODE, false)

        setContent {
            AmityLivestreamPostTargetSelectionPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                isEditMode = isEdit
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_IS_EDIT_MODE = "is_edit_mode"

        fun newIntent(
            context: Context,
            isEditMode : Boolean? = false,
        ): Intent {
            return Intent(
                context,
                AmityLivestreamPostTargetSelectionPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_IS_EDIT_MODE, isEditMode)
            }
        }
    }
}
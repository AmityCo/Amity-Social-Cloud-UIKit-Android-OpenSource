package com.amity.socialcloud.uikit.community.compose.community.bycategory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class AmityCommunitiesByCategoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val categoryId = getCategoryId(intent)
        setContent {
            AmityCommunitiesByCategoryPage(
                categoryId = categoryId,
            )
        }
    }

    companion object {
        const val EXTRA_PARAM_CATEGORY_ID = "categoryId"

        fun newIntent(
            context: Context,
            categoryId: String,
        ): Intent {
            return Intent(
                context,
                AmityCommunitiesByCategoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_CATEGORY_ID, categoryId)
            }
        }

        fun getCategoryId(intent: Intent): String {
            return intent.getStringExtra(EXTRA_PARAM_CATEGORY_ID) ?: ""
        }
    }
}
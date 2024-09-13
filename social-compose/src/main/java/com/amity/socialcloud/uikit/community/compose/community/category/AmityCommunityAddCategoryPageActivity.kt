package com.amity.socialcloud.uikit.community.compose.community.category

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.utils.closePageWithResult

class AmityCommunityAddCategoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val categories = getCategories(intent)

        setContent {
            AmityCommunityAddCategoryPage(
                categories = categories,
            ) {
                val intent = Intent()
                intent.putExtra(EXTRA_PARAM_CATEGORIES, it.toTypedArray())
                closePageWithResult(Activity.RESULT_OK, intent)
            }
        }
    }

    companion object {
        const val EXTRA_PARAM_CATEGORIES = "categories"

        fun newIntent(
            context: Context,
            categories: List<AmityCommunityCategory>,
        ): Intent {
            return Intent(
                context,
                AmityCommunityAddCategoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_CATEGORIES, categories.toTypedArray())
            }
        }

        fun getCategories(intent: Intent): List<AmityCommunityCategory> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(
                    EXTRA_PARAM_CATEGORIES,
                    AmityCommunityCategory::class.java
                )?.toList() ?: emptyList()
            } else {
                intent.getParcelableArrayExtra(EXTRA_PARAM_CATEGORIES)
                    ?.mapNotNull { it as? AmityCommunityCategory }
                    ?.toList() ?: emptyList()
            }
        }
    }
}
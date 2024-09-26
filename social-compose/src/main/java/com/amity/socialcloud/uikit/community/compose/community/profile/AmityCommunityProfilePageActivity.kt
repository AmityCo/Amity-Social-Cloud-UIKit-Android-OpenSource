package com.amity.socialcloud.uikit.community.compose.community.profile

import AmityCommunityProfilePage
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityCommunityProfilePageActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		val communityId = intent.getStringExtra(EXTRA_PARAM_COMMUNITY_ID) ?: ""

		setContent {
			AmityCommunityProfilePage(communityId = communityId)
		}
	}

	companion object {
		private const val EXTRA_PARAM_COMMUNITY_ID = "community_id"

		fun newIntent(
			context: Context,
			communityId: String
		): Intent {
			return Intent(
				context,
				AmityCommunityProfilePageActivity::class.java
			).apply {
				putExtra(EXTRA_PARAM_COMMUNITY_ID, communityId)
			}
		}
	}
}
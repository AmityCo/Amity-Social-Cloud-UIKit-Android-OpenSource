package com.amity.socialcloud.uikit.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.chat.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.chat.messages.AmityMessageListActivity
import com.amity.socialcloud.uikit.community.home.activity.AmityCommunityHomePageActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityCustomPostRankingFeedActivity
import com.amity.socialcloud.uikit.community.utils.AmityCommunityNavigation
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityFeatureListBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityFeatureListActivity : AppCompatActivity() {

    private val binding: AmityActivityFeatureListBinding by lazy {
        AmityActivityFeatureListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkNotificationPermission()

        binding.apply {
            communityHome.setOnClickListener {
                val communityIntent = Intent(
                    this@AmityFeatureListActivity,
                    AmityCommunityHomePageActivity::class.java
                )
                startActivity(communityIntent)
            }

            customRankingFeed.setOnClickListener {
                val communityIntent = Intent(
                    this@AmityFeatureListActivity,
                    AmityCustomPostRankingFeedActivity::class.java
                )
                startActivity(communityIntent)
            }

            chatHome.setOnClickListener {
                val chatIntent =
                    Intent(this@AmityFeatureListActivity, AmityChatHomePageActivity::class.java)
                startActivity(chatIntent)
            }

            userProfile.setOnClickListener {
                AmityCommunityNavigation.navigateToUserProfile(
                    this@AmityFeatureListActivity,
                    AmityCoreClient.getUserId()
                )
            }

            textComposebar.setOnClickListener {
                presentJoinDialog(callback = { d, input ->
                    run {
                        val channelId = input.toString()
                        joinChannel(channelId)
                    }
                })
            }

            customPostCreation.setOnClickListener {
                startActivity(
                    Intent(
                        this@AmityFeatureListActivity,
                        AmityPostCreatorSettingsActivity::class.java
                    )
                )
            }
        }
    }

    private fun presentJoinDialog(callback: InputCallback) {
        MaterialDialog(this).show {
            title(text = "Join Channel")
            input(
                inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
                hint = "Channel id",
                allowEmpty = false,
                callback = callback
            )
        }
    }

    private fun joinChannel(channelId: String) {
        AmityChatClient.newChannelRepository()
            .joinChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                val chatListIntent = AmityMessageListActivity.newIntent(this, channelId, true)
                startActivity(chatListIntent)
            }
            .subscribe()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val status = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (status != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "App can't send notification", Toast.LENGTH_SHORT).show()
        }
    }
}
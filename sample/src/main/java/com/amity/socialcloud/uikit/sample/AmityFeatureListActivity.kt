package com.amity.socialcloud.uikit.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.chat.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.chat.messages.AmityMessageListActivity
import com.amity.socialcloud.uikit.community.compose.community.category.AmityAllCategoriesPageActivity
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageActivity
import com.amity.socialcloud.uikit.community.home.activity.AmityCommunityHomePageActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityCustomPostRankingFeedActivity
import com.amity.socialcloud.uikit.community.utils.AmityCommunityNavigation
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityFeatureListBinding
import com.amity.socialcloud.uikit.sample.liveChat.AmityLiveChatListActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityFeatureListActivity : AppCompatActivity() {

    private val binding: AmityActivityFeatureListBinding by lazy {
        AmityActivityFeatureListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkNotificationPermission()

        val context = this
        binding.apply {
            communityHome.setOnClickListener {
                val communityIntent = Intent(
                    this@AmityFeatureListActivity,
                    AmityCommunityHomePageActivity::class.java
                )
                startActivity(communityIntent)
            }

            communityHomeV4.setOnClickListener {
                val communityIntent = Intent(
                    this@AmityFeatureListActivity,
                    AmitySocialHomePageActivity::class.java
                )
                startActivity(communityIntent)
            }

            socialV4Compatible.setOnClickListener {
                startActivity(
                    AmityCommunityHomePageActivity.newIntent(
                        this@AmityFeatureListActivity,
                        useNewsFeedV4 = true
                    )
                )
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

            userProfileV4.setOnClickListener {
                AmityCommunityNavigation.navigateToUserProfileV4(
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

            liveChat.setOnClickListener {
                startActivity(
                    Intent(
                        this@AmityFeatureListActivity,
                        AmityLiveChatListActivity::class.java
                    )
                )
            }

            configSync.setOnClickListener {
                configSync.isEnabled = false
                Log.d("UIKitV4", "syncing config")
                val delayForTesting = 5L
                Completable.complete()
                    .delay(delayForTesting, java.util.concurrent.TimeUnit.SECONDS)
                    .andThen(
                AmityUIKit4Manager.syncNetworkConfig()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        Log.d("UIKitV4", "sync complete")
                        Toast.makeText(context, "config synced", Toast.LENGTH_SHORT).show()
                    }
                    .doOnError {
                        Log.d("UIKitV4", "sync failed")
                        it.printStackTrace()
                        Toast.makeText(context, "config sync failed: " + it.message , Toast.LENGTH_SHORT).show()
                    }
                    .doFinally {
                        configSync.isEnabled = true
                    })

                    .subscribe()
            }

            playground.setOnClickListener {
                val intent = Intent(
                    this@AmityFeatureListActivity,
                    AmityPlaygroundActivity::class.java
                )
                startActivity(intent)
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
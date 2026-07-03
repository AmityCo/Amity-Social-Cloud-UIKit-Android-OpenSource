package com.amity.socialcloud.uikit.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.visitor.AmityVisitorUsageLimitPageActivity
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
            communityHomeV4.setOnClickListener {
                val communityIntent = Intent(
                    this@AmityFeatureListActivity,
                    AmitySocialHomePageActivity::class.java
                )
                startActivity(communityIntent)
            }

            chatHome.setOnClickListener {
                val chatIntent =
                    Intent(this@AmityFeatureListActivity, AmityChatHomePageActivity::class.java)
                startActivity(chatIntent)
            }

            userProfileV4.setOnClickListener {
                startActivity(
                    AmityUserProfilePageActivity.newIntent(
                        this@AmityFeatureListActivity,
                        AmityCoreClient.getUserId()
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
                                Toast.makeText(context, "config sync failed: " + it.message, Toast.LENGTH_SHORT).show()
                            }
                            .doFinally {
                                configSync.isEnabled = true
                            })
                    .subscribe()
            }

            visitorUsageLimitPage.setOnClickListener {
                startActivity(AmityVisitorUsageLimitPageActivity.newIntent(this@AmityFeatureListActivity))
            }
        }
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

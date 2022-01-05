package com.amity.socialcloud.uikit.community.notificationsettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityPushNotificationsSettingsBinding
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID

class AmityCommunityNotificationsSettingsActivity : AppCompatActivity(), AmityToolBarClickListener {

    private lateinit var binding: AmityActivityPushNotificationsSettingsBinding

    companion object {
        fun newIntent(context: Context, communityId: String): Intent =
            Intent(context, AmityCommunityNotificationsSettingsActivity::class.java).apply {
                putExtra(EXTRA_PARAM_COMMUNITY_ID, communityId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AmityActivityPushNotificationsSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        loadFragment()
    }

    private fun initToolbar() {
        binding.pushNotificationToolBar.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.amity_ic_arrow_back
            )
        )
        binding.pushNotificationToolBar.setClickListener(this)
        binding.pushNotificationToolBar.setLeftString(getString(R.string.amity_notification_settings))

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(binding.pushNotificationToolBar)
    }

    private fun loadFragment() {
        val communityId = intent.extras?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val builder = AmityCommunityNotificationSettingsFragment.newInstance(communityId)

        val fragment = builder.build()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    override fun leftIconClick() {
        this.finish()
    }

    override fun rightIconClick() {

    }

}
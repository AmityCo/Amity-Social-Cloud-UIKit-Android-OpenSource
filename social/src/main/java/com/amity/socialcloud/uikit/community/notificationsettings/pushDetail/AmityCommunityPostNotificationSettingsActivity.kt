package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityPushSettingsDetailBinding
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_NOTIFICATION_SETTING_TYPE

class AmityCommunityPostNotificationSettingsActivity : AppCompatActivity(),
    AmityToolBarClickListener {
    private val viewModel: AmityPushSettingsDetailViewModel by viewModels()
    private lateinit var fragment: AmityCommunityBaseNotificationSettingsFragment
    private lateinit var binding: AmityActivityPushSettingsDetailBinding


    companion object {
        fun newIntent(context: Context, communityId: String, type: SettingType): Intent {
            return Intent(
                context,
                AmityCommunityPostNotificationSettingsActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY_ID, communityId)
                putExtra(EXTRA_PARAM_NOTIFICATION_SETTING_TYPE, type.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AmityActivityPushSettingsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        loadFragment()

        viewModel.initialStateChanged.observe(this, Observer {
            binding.pushDetailToolBar.setRightStringActive(it)
        })
    }

    private fun initToolbar() {
        binding.pushDetailToolBar.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.amity_ic_arrow_back
            )
        )
        binding.pushDetailToolBar.setClickListener(this)
        val postType = intent?.extras?.getString(EXTRA_PARAM_NOTIFICATION_SETTING_TYPE)
        val leftString = when (postType) {
            SettingType.POSTS.name -> getString(R.string.amity_Posts)
            SettingType.COMMENTS.name -> getString(R.string.amity_comments)
            SettingType.STORIES.name -> getString(R.string.amity_stories)
            else -> ""
        }
        binding.pushDetailToolBar.setLeftString(leftString)

        binding.pushDetailToolBar.setRightString(getString(R.string.amity_save))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(binding.pushDetailToolBar)
    }

    private fun loadFragment() {
        val communityId = intent.extras?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
        val settingType = intent.extras?.getString(EXTRA_PARAM_NOTIFICATION_SETTING_TYPE)
            ?: SettingType.POSTS.name
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragment = when (settingType) {
            SettingType.POSTS.name -> AmityCommunityPostNotificationSettingsFragment.newInstance(
                communityId
            ).build()

            SettingType.COMMENTS.name -> AmityCommunityCommentNotificationSettingsFragment.newInstance(
                communityId
            ).build()

            else -> AmityCommunityStoryNotificationSettingsFragment.newInstance(communityId).build()
        }

        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    override fun leftIconClick() {
        this.finish()
    }

    override fun rightIconClick() {
        fragment.save()
    }

    enum class SettingType {
        POSTS,
        COMMENTS,
        STORIES
    }
}
package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_NOTIFICATION_SETTING_TYPE

class AmityCommunityPostNotificationSettingsFragment :
    AmityCommunityBaseNotificationSettingsFragment() {


    private val viewModel: AmityPushSettingsDetailViewModel by activityViewModels()

    companion object {
        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val communityId = arguments?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
        val settingType =
            arguments?.getString(EXTRA_PARAM_NOTIFICATION_SETTING_TYPE) ?: DEFAULT_SETTING_TYPE
        viewModel.setInitialState(communityId, settingType)
    }

    class Builder internal constructor() {
        private var communityId: String? = null

        fun build(): AmityCommunityPostNotificationSettingsFragment {
            return AmityCommunityPostNotificationSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_COMMUNITY_ID, communityId)
                    putString(EXTRA_PARAM_NOTIFICATION_SETTING_TYPE, DEFAULT_SETTING_TYPE)
                }
            }
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }
    }
}

private val DEFAULT_SETTING_TYPE =
    AmityCommunityPostNotificationSettingsActivity.SettingType.POSTS.name

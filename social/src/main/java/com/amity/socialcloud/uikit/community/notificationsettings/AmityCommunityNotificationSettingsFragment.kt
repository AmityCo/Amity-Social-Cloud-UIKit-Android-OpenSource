package com.amity.socialcloud.uikit.community.notificationsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentPushNotificationSettingsBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.notificationsettings.pushDetail.AmityCommunityPostNotificationSettingsActivity
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItemAdapter
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxFragment

class AmityCommunityNotificationSettingsFragment : RxFragment() {

    private val viewModel: AmityPushNotificationSettingsViewModel by viewModels()
    private val settingsListAdapter = AmitySettingsItemAdapter()
    private lateinit var binding: AmityFragmentPushNotificationSettingsBinding

    companion object {
        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.communityId = arguments?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentPushNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    internal fun toggleAllSettings(isChecked: Boolean) {
        viewModel.revertToggleState(isChecked)
        updateGlobalPushSettings(isChecked)
    }

    private fun setUpRecyclerView() {
        binding.progressbar.visibility = View.VISIBLE
        binding.rvNotificationSettings.layoutManager = LinearLayoutManager(context)
        binding.rvNotificationSettings.adapter = settingsListAdapter
        getPushNotificationMenuItems()
    }

    private fun getPushNotificationMenuItems() {
        viewModel.getPushNotificationSettings(
            communityId = viewModel.communityId,
            onDataLoaded = this::getSettingsItems,
            onDataError = this::showErrorLayout
        ).untilLifecycleEnd(this)
            .subscribe()

    }

    private fun getSettingsItems(value: Boolean) {
        viewModel.getPushNotificationItems(
            menuCreator = AmityPushNotificationMenuCreatorImpl(this),
            startValue = value,
            onResult = this::renderItems
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun renderItems(items: List<AmitySettingsItem>) {
        settingsListAdapter.setItems(items)
        binding.progressbar.visibility = View.GONE
    }

    fun navigateToNewPostSettings(
        communityId: String,
        type: AmityCommunityPostNotificationSettingsActivity.SettingType
    ) {
        val intent = AmityCommunityPostNotificationSettingsActivity.newIntent(
            requireContext(),
            communityId,
            type
        )
        startActivity(intent)
    }

    private fun updateGlobalPushSettings(value: Boolean) {
        viewModel.updatePushNotificationSettings(enable = value,
            onError = {
                errorDialog(
                    R.string.amity_unable_to_save,
                    R.string.amity_something_went_wrong_pls_try,
                    value
                )
            }).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showErrorLayout() {
        binding.rvNotificationSettings.visibility = View.GONE
        binding.errorLayout.parent.visibility = View.VISIBLE
    }

    private fun errorDialog(title: Int, description: Int, value: Boolean) {
        AmityAlertDialogUtil.showDialog(
            requireContext(),
            getString(title),
            getString(description),
            getString(R.string.amity_ok),
            null
        ) { dialog, which ->
            AmityAlertDialogUtil.checkConfirmDialog(isPositive = which, confirmed = {
                dialog.cancel()
                viewModel.revertToggleState(!value)
            })
        }
    }

    class Builder internal constructor() {
        private var communityId: String = ""
        fun build(): AmityCommunityNotificationSettingsFragment {
            return AmityCommunityNotificationSettingsFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_PARAM_COMMUNITY_ID, communityId) }
            }
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }
    }
}
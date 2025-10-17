package com.amity.socialcloud.uikit.community.setting

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil.checkConfirmDialog
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunitySettingsBinding
import com.amity.socialcloud.uikit.community.edit.AmityCommunityProfileActivity
import com.amity.socialcloud.uikit.community.members.AmityCommunityMemberSettingsActivity
import com.amity.socialcloud.uikit.community.notificationsettings.AmityCommunityNotificationsSettingsActivity
import com.amity.socialcloud.uikit.community.setting.postreview.AmityPostReviewSettingsActivity
import com.amity.socialcloud.uikit.community.setting.story.AmityStorySettingsActivity
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import java.util.Locale

class AmityCommunitySettingsFragment : RxFragment() {

    private val settingsListAdapter = AmitySettingsItemAdapter()
    private val viewModel: AmityCommunitySettingViewModel by activityViewModels()
    private lateinit var binding: AmityFragmentCommunitySettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingsListRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentCommunitySettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.communityId = arguments?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
    }

    private fun setupSettingsListRecyclerView() {
        binding.progressbar.visibility = View.VISIBLE
        binding.rvCommunitySettings.layoutManager = LinearLayoutManager(context)
        binding.rvCommunitySettings.adapter = settingsListAdapter
    }

    override fun onResume() {
        super.onResume()
        getGlobalPushNotificationSettings()
    }

    private fun getGlobalPushNotificationSettings() {
        viewModel.getGlobalPushNotificationSettings(
            onSuccess = this::getPushNotificationSettings,
            onError = this::showErrorLayout
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun getPushNotificationSettings() {
        viewModel.getPushNotificationSettings(
            viewModel.communityId!!,
            onDataLoaded = {
                getCommunitySettingsItems()
            },
            onDataError = this::showErrorLayout
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun getCommunitySettingsItems() {
        viewModel.getSettingsItems(
            menuCreator = AmityCommunitySettingsMenuCreatorImpl(this),
            onResult = this::renderItems,
            onError = this::showErrorLayout
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showErrorLayout() {
        binding.rvCommunitySettings.visibility = View.GONE
        binding.errorLayout.parent.visibility = View.VISIBLE
    }

    private fun renderItems(items: List<AmitySettingsItem>) {
        settingsListAdapter.setItems(items)
        binding.progressbar.visibility = View.GONE
    }

    internal fun confirmCloseCommunity() {
        AmityAlertDialogUtil.showDialog(requireContext(),
            "${getString(R.string.amity_close_community)}?",
            getString(R.string.amity_close_community_msg),
            getString(R.string.amity_close).uppercase(Locale.getDefault()),
            getString(R.string.amity_cancel).uppercase(Locale.getDefault()),
            DialogInterface.OnClickListener { dialog, which ->
                checkConfirmDialog(
                    isPositive = which,
                    confirmed = {
                        viewModel.closeCommunity(
                            onCloseSuccess = (requireActivity()::finish),
                            onCloseError = {
                                errorDialog(
                                    title = R.string.amity_close_community_error_title,
                                    description = R.string.amity_something_went_wrong_pls_try
                                )
                            })
                            .untilLifecycleEnd(this)
                            .subscribe()
                    },
                    cancel = { dialog.cancel() })
            })
    }

    private fun confirmLastModeratorLeaveCommunity() {
        AmityAlertDialogUtil.showDialog(requireContext(),
            "${getString(R.string.amity_leave_community)}?",
            getString(R.string.amity_last_moderator_leave_community_msg),
            getString(R.string.amity_close).uppercase(Locale.getDefault()),
            getString(R.string.amity_cancel).uppercase(Locale.getDefault()),
            DialogInterface.OnClickListener { dialog, which ->
                checkConfirmDialog(
                    isPositive = which,
                    confirmed = {
                        viewModel.closeCommunity(
                            onCloseSuccess = (requireActivity()::finish),
                            onCloseError = {
                                errorDialog(
                                    title = R.string.amity_close_community_error_title,
                                    description = R.string.amity_something_went_wrong_pls_try
                                )
                            })
                            .untilLifecycleEnd(this)
                            .subscribe()
                    },
                    cancel = { dialog.cancel() })
            })
    }

    internal fun confirmLeaveCommunity() {
        AmityAlertDialogUtil.showDialog(requireContext(),
            "${getString(R.string.amity_leave_community)}?",
            getString(R.string.amity_leave_community_msg),
            getString(R.string.amity_leave).uppercase(Locale.getDefault()),
            getString(R.string.amity_cancel).uppercase(Locale.getDefault()),
            DialogInterface.OnClickListener { dialog, which ->
                checkConfirmDialog(
                    isPositive = which,
                    confirmed = {
                        viewModel.leaveCommunity(
                            onLeaveSuccess = (requireActivity()::finish),
                            onLeaveError = {
                                errorDialog(
                                    title = R.string.amity_leave_community_error_title,
                                    description = R.string.amity_something_went_wrong_pls_try
                                )
                            })
                            .untilLifecycleEnd(this)
                            .subscribe()
                    },
                    cancel = { dialog.cancel() })
            })
    }

    internal fun confirmModeratorLeaveCommunity() {
        AmityAlertDialogUtil.showDialog(requireContext(),
            "${getString(R.string.amity_leave_community)}?",
            getString(R.string.amity_moderator_leave_community_msg),
            getString(R.string.amity_leave).uppercase(Locale.getDefault()),
            getString(R.string.amity_cancel).uppercase(Locale.getDefault()),
            DialogInterface.OnClickListener { dialog, which ->
                checkConfirmDialog(
                    isPositive = which,
                    confirmed = {
                        viewModel.leaveCommunity(
                            onLeaveSuccess = (requireActivity()::finish),
                            onLeaveError = {
                                when (it) {
                                    AmityError.USER_IS_LAST_MODERATOR -> {
                                        errorDialog(
                                            title = R.string.amity_leave_community_error_title,
                                            description = R.string.amity_moderator_leave_community_error_msg
                                        )
                                    }
                                    AmityError.USER_IS_LAST_MEMBER -> {
                                        confirmLastModeratorLeaveCommunity()
                                    }
                                    else -> {
                                        errorDialog(
                                            title = R.string.amity_leave_community_error_title,
                                            description = R.string.amity_something_went_wrong_pls_try
                                        )
                                    }
                                }
                            })
                            .untilLifecycleEnd(this)
                            .subscribe()
                    },
                    cancel = { dialog.cancel() })
            })
    }

    internal fun navigateToCommunityProfile(communityId: String) {
        val intent = AmityCommunityProfileActivity.newIntent(requireContext(), communityId)
        startActivity(intent)
    }

    internal fun navigateToPushNotificationSettings(communityId: String) {
        val intent = AmityCommunityNotificationsSettingsActivity.newIntent(
            requireContext(),
            communityId
        )
        startActivity(intent)
    }

    internal fun navigateToCommunityMemberSettings(communityId: String) {
        val intent = AmityCommunityMemberSettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId!!,
            true
        )
        startActivity(intent)
    }

    internal fun navigateToPostReview() {
        val intent = AmityPostReviewSettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId!!
        )
        startActivity(intent)
    }

    internal fun navigateToStorySetting() {
        val intent = AmityStorySettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId!!
        )
        startActivity(intent)
    }

    private fun errorDialog(title: Int, description: Int) {
        AmityAlertDialogUtil.showDialog(requireContext(),
            getString(title),
            getString(description),
            getString(R.string.amity_ok),
            null,
            DialogInterface.OnClickListener { dialog, which ->
                checkConfirmDialog(isPositive = which, confirmed = dialog::cancel)
            })
    }

    class Builder internal constructor() {
        private lateinit var communityId: String

        internal fun communityId(id: String): Builder {
            communityId = id
            return this
        }

        fun build(): AmityCommunitySettingsFragment {
            return AmityCommunitySettingsFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_PARAM_COMMUNITY_ID, communityId) }
            }
        }

    }

    companion object {
        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

}
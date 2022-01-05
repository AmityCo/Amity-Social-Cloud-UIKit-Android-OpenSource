package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.social.community.AmityCommunityNotificationEvent
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentPushSettingsDetailsBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItemAdapter
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.snackbar.Snackbar
import com.trello.rxlifecycle3.components.support.RxFragment

open class AmityCommunityBaseNotificationSettingsFragment internal constructor() :
    RxFragment(R.layout.amity_fragment_push_settings_details) {

    private val viewModel: AmityPushSettingsDetailViewModel by activityViewModels()
    private val settingsListAdapter = AmitySettingsItemAdapter()
    private lateinit var binding: AmityFragmentPushSettingsDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AmityFragmentPushSettingsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.progressbar.visibility = View.VISIBLE
        binding.rvNewPostSettings.layoutManager = LinearLayoutManager(context)
        binding.rvNewPostSettings.adapter = settingsListAdapter
        viewModel.getGlobalPushNotificationSettings(
            onSuccess = this::getDetailSettingsItem,
            onError = this::showErrorLayout
        ).untilLifecycleEnd(this).subscribe()
    }

    private fun getDetailSettingsItem() {
        viewModel.getDetailSettingsItem(
            postMenuCreator = AmityPostMenuCreatorImpl(this),
            commentMenuCreator = AmityCommentMenuCreatorImpl(this),
            onResult = this::renderItems,
            onError = this::showErrorLayout
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showErrorLayout() {
        binding.rvNewPostSettings.visibility = View.GONE
        binding.errorLayout.parent.visibility = View.VISIBLE
    }

    private fun renderItems(items: List<AmitySettingsItem>) {
        settingsListAdapter.setItems(items)
        binding.progressbar.visibility = View.GONE
    }

    internal fun toggleReactPost(value: Int) {
        viewModel.changeState(AmityCommunityNotificationEvent.POST_REACTED.toString(), value)
    }

    internal fun toggleNewPost(value: Int) {
        viewModel.changeState(AmityCommunityNotificationEvent.POST_CREATED.toString(), value)
    }

    internal fun toggleReactComment(value: Int) {
        viewModel.changeState(AmityCommunityNotificationEvent.COMMENT_REACTED.toString(), value)
    }

    internal fun toggleNewComment(value: Int) {
        viewModel.changeState(AmityCommunityNotificationEvent.COMMENT_CREATED.toString(), value)
    }

    internal fun toggleReplyComment(value: Int) {
        viewModel.changeState(AmityCommunityNotificationEvent.COMMENT_REPLIED.toString(), value)
    }

    internal fun save() {
        viewModel.updatePushNotificationSettings(
            onComplete = {
                showSnackBar()
            },
            onError = {
                errorDialog(
                    R.string.amity_unable_to_save,
                    R.string.amity_something_went_wrong_pls_try
                )
            }).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun revertState() {
        viewModel.resetState()
        if (viewModel.settingType == AmityCommunityPostNotificationSettingsActivity.SettingType.POSTS.name) {
            settingsListAdapter.setItems(
                viewModel.createPostSettingsItem(
                    AmityPostMenuCreatorImpl(
                        this
                    )
                )
            )
        } else {
            settingsListAdapter.setItems(
                viewModel.createCommentSettingsItem(
                    AmityCommentMenuCreatorImpl(this)
                )
            )
        }
    }

    private fun errorDialog(title: Int, description: Int) {
        AmityAlertDialogUtil.showDialog(requireContext(),
            getString(title),
            getString(description),
            getString(R.string.amity_ok),
            null,
            DialogInterface.OnClickListener { dialog, which ->
                AmityAlertDialogUtil.checkConfirmDialog(isPositive = which, confirmed = {
                    dialog.cancel()
                    revertState()
                })
            })
    }

    private fun showSnackBar() {
        Snackbar.make(binding.rvNewPostSettings, R.string.amity_saved, Snackbar.LENGTH_SHORT).show()
    }
}
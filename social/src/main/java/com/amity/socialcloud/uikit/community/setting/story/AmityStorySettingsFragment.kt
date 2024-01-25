package com.amity.socialcloud.uikit.community.setting.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentStorySettingsBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItemAdapter
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment

class AmityStorySettingsFragment : RxFragment() {

    private var _binding: AmityFragmentStorySettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsListAdapter = AmitySettingsItemAdapter()
    private lateinit var viewModel: AmityStorySettingsViewModel

    companion object {
        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AmityFragmentStorySettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityStorySettingsViewModel::class.java)
        setupStorySettingsListRecyclerView()
    }

    internal fun toggleAllowCommentEvent(isChecked: Boolean) {
        viewModel.toggleDecision(
            isChecked = isChecked,
            turnOffEvent = {
                viewModel.turnOff(
                    onError = {
                        viewModel.revertToggleState()
                        errorDialog(
                            title = R.string.amity_unable_turn_off_story_allow_comment_title,
                            description = R.string.amity_something_went_wrong_pls_try
                        )
                    }
                )
                    .untilLifecycleEnd(this)
                    .subscribe()
            },
            turnOnEvent = {
                viewModel.turnOn(
                    onError = {
                        viewModel.revertToggleState()
                        errorDialog(
                            title = R.string.amity_unable_turn_on_story_allow_comment_title,
                            description = R.string.amity_something_went_wrong_pls_try
                        )
                    })
                    .untilLifecycleEnd(this)
                    .subscribe()
            })
    }

    private fun setupStorySettingsListRecyclerView() {
        binding.rvStorySettings.layoutManager = LinearLayoutManager(context)
        binding.rvStorySettings.adapter = settingsListAdapter
        getStorySettingItems()
    }

    private fun getStorySettingItems() {
        viewModel.getSettingsItems(
            menuCreator = AmityStorySettingsMenuCreatorImpl(this),
            onResult = this::renderItems
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun renderItems(items: List<AmitySettingsItem>) {
        settingsListAdapter.setItems(items)
    }

    private fun errorDialog(title: Int, description: Int) {
        AmityAlertDialogUtil.showDialog(
            requireContext(),
            getString(title),
            getString(description),
            getString(R.string.amity_ok),
            null
        ) { dialog, which ->
            AmityAlertDialogUtil.checkConfirmDialog(
                isPositive = which, confirmed = dialog::cancel
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder internal constructor() {
        private var communityId: String? = null

        internal fun communityId(id: String): Builder {
            communityId = id
            return this
        }

        fun build(activity: AppCompatActivity): AmityStorySettingsFragment {
            val viewModel =
                ViewModelProvider(activity).get(AmityStorySettingsViewModel::class.java)
            viewModel.communityId = communityId
            return AmityStorySettingsFragment()
        }
    }

}
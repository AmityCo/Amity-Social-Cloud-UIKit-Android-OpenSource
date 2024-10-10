package com.amity.socialcloud.uikit.chat.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityFragmentChatHomePageBinding
import com.amity.socialcloud.uikit.chat.home.AmityChatHomePageViewModel
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.messages.AmityMessageListActivity
import com.amity.socialcloud.uikit.chat.recent.fragment.AmityRecentChatFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.contract.AmityPickMemberContract
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityChatHomePageFragment : Fragment() {
    private lateinit var mViewModel: AmityChatHomePageViewModel
    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter

    private var _binding: AmityFragmentChatHomePageBinding? = null
    private val binding get() = _binding!!

    private val selectMembers = registerForActivityResult(AmityPickMemberContract()) { userList ->
        if (userList.isNotEmpty()) {
            view?.showSnackBar(msg = getString(R.string.amity_channel_creation_loading))
            mViewModel.createChat(selectedMembers = userList,
                onChatCreateSuccess = { channelId: String ->
                    val chatListIntent =
                        AmityMessageListActivity.newIntent(requireContext(), channelId)
                    startActivity(chatListIntent)
                },
                onChatCreateFailed = { view?.showSnackBar(msg = getString(R.string.amity_channel_creation_error)) })
                .untilLifecycleEnd(this.requireView())
                .subscribe()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel =
            ViewModelProvider(requireActivity()).get(AmityChatHomePageViewModel::class.java)
        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AmityFragmentChatHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initTabLayout()
    }

    private fun initToolbar() {
        binding.chatHomeToolBar.setLeftString(getString(R.string.amity_chat))
        (activity as AppCompatActivity).supportActionBar?.displayOptions =
            ActionBar.DISPLAY_SHOW_CUSTOM
        (activity as AppCompatActivity).setSupportActionBar(binding.chatHomeToolBar as Toolbar)
        setHasOptionsMenu(true)
    }

    private fun initTabLayout() {
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_recent_chat),
                    getRecentChatFragment()
                )
            )
        )
        binding.tabLayout.setAdapter(fragmentStateAdapter)
    }

    private fun getRecentChatFragment(): Fragment {
        return if (mViewModel.recentChatFragmentDelegate != null) {
            mViewModel.recentChatFragmentDelegate!!.recentChatFragment()
        } else {
            val builder = AmityRecentChatFragment.Builder()
            if (mViewModel.recentChatItemClickListener != null) {
                builder.recentChatItemClickListener(mViewModel.recentChatItemClickListener!!)
            }
            builder.build(activity as AppCompatActivity)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.amity_chat_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create) {
            navigateToCreateGroupChat()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToCreateGroupChat() {
        selectMembers.launch(arrayListOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder internal constructor() {
        private var recentChatFragmentDelegate: AmityRecentChatFragmentDelegate? = null

        fun build(activity: AppCompatActivity): AmityChatHomePageFragment {
            val fragment = AmityChatHomePageFragment()
            fragment.mViewModel =
                ViewModelProvider(activity).get(AmityChatHomePageViewModel::class.java)
            return fragment
        }

        fun recentChatFragmentDelegate(delegate: AmityRecentChatFragmentDelegate): Builder {
            recentChatFragmentDelegate = delegate
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}
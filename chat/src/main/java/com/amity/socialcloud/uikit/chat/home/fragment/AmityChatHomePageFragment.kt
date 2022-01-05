package com.amity.socialcloud.uikit.chat.home.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.directory.fragment.AmityDirectoryFragment
import com.amity.socialcloud.uikit.chat.home.AmityChatHomePageViewModel
import com.amity.socialcloud.uikit.chat.home.callback.AmityDirectoryFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.chat.recent.fragment.AmityRecentChatFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import kotlinx.android.synthetic.main.amity_fragment_chat_home_page.*

class AmityChatHomePageFragment private constructor() : Fragment() {
    private lateinit var mViewModel: AmityChatHomePageViewModel
    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(AmityChatHomePageViewModel::class.java)
        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amity_fragment_chat_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initTabLayout()
    }

    private fun initToolbar() {
        chatHomeToolBar.setLeftString(getString(R.string.amity_chat))
        (activity as AppCompatActivity).supportActionBar?.displayOptions =
            ActionBar.DISPLAY_SHOW_CUSTOM
        (activity as AppCompatActivity).setSupportActionBar(chatHomeToolBar as Toolbar)
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
        tabLayout.setAdapter(fragmentStateAdapter)
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

    private fun getDirectoryFragment(): Fragment {
        if (mViewModel.directoryFragmentDelegate != null) {
            return mViewModel.directoryFragmentDelegate!!.directoryFragment()
        }
        return AmityDirectoryFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.eko_chat_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            // Do nothing
        } else if (item.itemId == R.id.create) {
            navigateToCreateGroupChat()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToCreateGroupChat() {
        //val intent = Intent(activity, EkoCreateChatActivity::class.java)
        //startActivity(intent)
    }


    class Builder internal constructor(){

        private var mListener: AmityRecentChatItemClickListener? = null
        private var recentChatFragmentDelegate: AmityRecentChatFragmentDelegate? = null
        private var directoryFragmentDelegate: AmityDirectoryFragmentDelegate? = null

        fun build(activity: AppCompatActivity): AmityChatHomePageFragment {
            val fragment = AmityChatHomePageFragment()
            fragment.mViewModel = ViewModelProvider(activity).get(AmityChatHomePageViewModel::class.java)
            fragment.mViewModel.recentChatItemClickListener = mListener
            fragment.mViewModel.recentChatFragmentDelegate = this.recentChatFragmentDelegate
            fragment.mViewModel.directoryFragmentDelegate = this.directoryFragmentDelegate
            return fragment
        }

        private fun recentChatItemClickListener(listener: AmityRecentChatItemClickListener): Builder {
            mListener = listener
            return this
        }

        fun recentChatFragmentDelegate(delegate: AmityRecentChatFragmentDelegate): Builder {
            recentChatFragmentDelegate = delegate
            return this
        }

        private fun directoryFragmentDelegate(delegate: AmityDirectoryFragmentDelegate): Builder {
            directoryFragmentDelegate = delegate
            return this
        }
    }

    companion object {
        fun newInstance() : Builder {
            return Builder()
        }
    }

}
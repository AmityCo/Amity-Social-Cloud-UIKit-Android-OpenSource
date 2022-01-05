package com.amity.socialcloud.uikit.chat.recent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityFragmentRecentChatBinding
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.chat.messages.AmityMessageListActivity
import com.amity.socialcloud.uikit.chat.recent.adapter.AmityRecentChatAdapter
import com.amity.socialcloud.uikit.chat.util.AmityRecentItemDecoration
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.amity_fragment_recent_chat.*

class AmityRecentChatFragment private constructor() : Fragment(), AmityRecentChatItemClickListener {
    private lateinit var mViewModel: AmityRecentChatViewModel

    private lateinit var mAdapter: AmityRecentChatAdapter
    private lateinit var recentChatDisposable: Disposable
    private lateinit var mBinding: AmityFragmentRecentChatBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(requireActivity()).get(AmityRecentChatViewModel::class.java)
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.amity_fragment_recent_chat, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = AmityRecentChatAdapter()
        mAdapter.setCommunityChatItemClickListener(this)
        rvRecentChat.layoutManager = LinearLayoutManager(requireContext())
        rvRecentChat.adapter = mAdapter
        rvRecentChat.addItemDecoration(
                AmityRecentItemDecoration(
                        requireContext(),
                        resources.getDimensionPixelSize(R.dimen.amity_padding_m2)
                )
        )
        getRecentChatData()
    }

    private fun getRecentChatData() {
        recentChatDisposable = mViewModel.getRecentChat().subscribe { chatList ->
            if (chatList.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                rvRecentChat.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                rvRecentChat.visibility = View.VISIBLE
                mAdapter.submitList(chatList)
            }
        }

    }

    override fun onRecentChatItemClick(channelId: String) {
        if (mViewModel.recentChatItemClickListener != null) {
            mViewModel.recentChatItemClickListener?.onRecentChatItemClick(channelId)
        } else {
            val chatListIntent = AmityMessageListActivity.newIntent(requireContext(), channelId)
            startActivity(chatListIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!recentChatDisposable.isDisposed) {
            recentChatDisposable.dispose()
        }
    }

    class Builder internal constructor() {
        private var mListener: AmityRecentChatItemClickListener? = null

        fun build(activity: AppCompatActivity): AmityRecentChatFragment {
            val fragment = AmityRecentChatFragment()
            fragment.mViewModel = ViewModelProvider(activity).get(AmityRecentChatViewModel::class.java)
            fragment.mViewModel.recentChatItemClickListener = mListener
            return fragment
        }

        fun recentChatItemClickListener(listener: AmityRecentChatItemClickListener): Builder {
            mListener = listener
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}
package com.amity.socialcloud.uikit.chat.recent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityFragmentRecentChatBinding
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.chat.messages.AmityMessageListActivity
import com.amity.socialcloud.uikit.chat.recent.adapter.AmityRecentChatAdapter
import com.amity.socialcloud.uikit.chat.util.AmityRecentItemDecoration
import io.reactivex.rxjava3.disposables.Disposable

class AmityRecentChatFragment : Fragment(), AmityRecentChatItemClickListener {
    private lateinit var mViewModel: AmityRecentChatViewModel

    private lateinit var mAdapter: AmityRecentChatAdapter
    private lateinit var recentChatDisposable: Disposable
    private lateinit var binding: AmityFragmentRecentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel = ViewModelProvider(requireActivity()).get(AmityRecentChatViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.amity_fragment_recent_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListener()
    }

    private fun initRecyclerView() {
        mAdapter = AmityRecentChatAdapter()
        mAdapter.setCommunityChatItemClickListener(this)
        binding.rvRecentChat.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = mAdapter
            this.addItemDecoration(
                AmityRecentItemDecoration(
                    requireContext(),
                    resources.getDimensionPixelSize(R.dimen.amity_padding_m2)
                )
            )
        }
        getRecentChatData()
    }

    private fun getRecentChatData() {
        recentChatDisposable = mViewModel.getRecentChat()
            .doOnNext { chatList ->
                mAdapter.submitData(lifecycle, chatList)
            }.subscribe()
    }

    private fun initListener() {
        mAdapter.addLoadStateListener { loadState ->
            loadState.run {
                if (source.append is LoadState.NotLoading
                    && source.append.endOfPaginationReached
                    && mAdapter.itemCount == 0
                ) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.rvRecentChat.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.rvRecentChat.visibility = View.VISIBLE
                }
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
            fragment.mViewModel =
                ViewModelProvider(activity).get(AmityRecentChatViewModel::class.java)
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
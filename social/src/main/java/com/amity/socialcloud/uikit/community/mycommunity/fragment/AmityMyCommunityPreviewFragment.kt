package com.amity.socialcloud.uikit.community.mycommunity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentMyCommunityListBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.mycommunity.activity.AmityMyCommunityActivity
import com.amity.socialcloud.uikit.community.mycommunity.adapter.AmityMyCommunityPreviewAdapter
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import com.amity.socialcloud.uikit.community.mycommunity.viewmodel.AmityMyCommunityListViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityMyCommunityPreviewFragment : AmityBaseFragment(),
    AmityMyCommunityItemClickListener {

    private val viewModel: AmityMyCommunityListViewModel by viewModels()
    private lateinit var adapter: AmityMyCommunityPreviewAdapter
    private lateinit var binding: AmityFragmentMyCommunityListBinding


    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            AmityFragmentMyCommunityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.ivMore.setOnClickListener {
            val myCommunityIntent = Intent(requireContext(), AmityMyCommunityActivity::class.java)
            startActivity(myCommunityIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        addItemTouchListener()
    }

    internal fun refresh() {
        getCommunityList()
        binding.rvMyCommunity.smoothScrollToPosition(0)
    }

    private fun initRecyclerView() {
        adapter = AmityMyCommunityPreviewAdapter(this)
        binding.rvMyCommunity.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMyCommunity.adapter = adapter
        binding.rvMyCommunity.itemAnimator = null
        binding.rvMyCommunity.addItemDecoration(
            AmityRecyclerViewItemDecoration(
                0, resources.getDimensionPixelSize(R.dimen.amity_padding_m1), 0,
                resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
            )
        )
        binding.rvMyCommunity.setHasFixedSize(true)
        getCommunityList()
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                if (adapter.itemCount == 0) {
                    binding.parent.visibility = View.GONE
                } else {
                    binding.parent.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getCommunityList() {
        viewModel.getCommunityList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .doOnNext { list ->
                adapter.submitData(lifecycle, list)
            }
            .subscribe()
    }

    private fun addItemTouchListener() {
        val touchListener = object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                return if (binding.rvMyCommunity.canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    false
                } else {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                    false
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        }
        binding.rvMyCommunity.addOnItemTouchListener(touchListener)
    }

    override fun onCommunitySelected(ekoCommunity: AmityCommunity?) {
        if (ekoCommunity != null) {
            val detailIntent = AmityCommunityProfilePageActivity.newIntent(
                requireContext(),
                ekoCommunity.getCommunityId()
            )
            startActivity(detailIntent)
        } else {
            val myCommunityIntent = Intent(requireContext(), AmityMyCommunityActivity::class.java)
            startActivity(myCommunityIntent)
        }
    }

    class Builder internal constructor() {
        fun build(): AmityMyCommunityPreviewFragment {
            return AmityMyCommunityPreviewFragment()
        }
    }
}
package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentSingleReactionBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityReactionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmitySingleReactionViewModel
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AmitySingleReactionFragment : AmityBaseFragment() {

    private lateinit var binding: AmityFragmentSingleReactionBinding
    private lateinit var viewModel: AmitySingleReactionViewModel
    private lateinit var adapter: AmityReactionAdapter

    private var isRefreshing = false

    private val emptyStatePublisher = PublishSubject.create<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentSingleReactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmitySingleReactionViewModel::class.java)

        setupRecyclerView()
        observeClickEvents()
        getReactions {
            onPageLoaded(it)
        }
    }

    private fun setupRecyclerView() {
        adapter = viewModel.createReactionAdapter()
        adapter.addLoadStateListener { loadStates ->
            when (val refreshState = loadStates.mediator?.refresh) {
                is LoadState.NotLoading -> {
                    handleLoadedState(adapter.itemCount, loadStates)
                }
                is LoadState.Error -> {
                    handleErrorState(AmityError.from(refreshState.error))
                }
                is LoadState.Loading -> {}
                else -> {}
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0) {
                    binding.rvReaction.smoothScrollToPosition(0)
                }
            }
        })

        binding.rvReaction.adapter = adapter

        emptyStatePublisher.toFlowable(BackpressureStrategy.BUFFER)
            .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .doOnNext { shouldShowEmptyState ->
                if (shouldShowEmptyState) {
                    handleEmptyState()
                }
            }
            .subscribe()
    }

    private fun getReactions(onPageLoaded: (reactions: PagingData<AmityReaction>) -> Unit) {
        binding.progressBar.visibility = View.VISIBLE
        val reactionName = arguments?.getString(REACTION_NAME)

        viewModel.getReactionPagingData(reactionName, onPageLoaded)
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun onPageLoaded(items: PagingData<AmityReaction>) {
        adapter.submitData(lifecycle, items)
    }

    private fun observeClickEvents() {
        viewModel.getUserClickEvents(
            onReceivedEvent = { viewModel.userClickListener.onClickUser(it) }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun handleLoadedState(itemCount: Int, loadStates: CombinedLoadStates) {
        binding.emptyLayout.visibility = View.GONE
        binding.rvReaction.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        if (isRefreshing) {
            binding.rvReaction.scrollToPosition(0)
            isRefreshing = false
        }

        if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount < 1
        ) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(true)
            }
        } else if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount > 0
        ) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(false)
            }
        }
    }

    private fun handleErrorState(error: AmityError) {
        binding.progressBar.visibility = View.GONE
        emptyStatePublisher.onNext(true)
    }

    private fun handleEmptyState() {
        binding.emptyLayout.visibility = View.VISIBLE
        binding.rvReaction.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    class Builder internal constructor() {

        private lateinit var referenceType: AmityReactionReferenceType
        private lateinit var referenceId: String
        private var reactionName: String? = null

        private var userClickListener: AmityUserClickListener? = null

        fun build(activity: AppCompatActivity): AmitySingleReactionFragment {
            val fragment = AmitySingleReactionFragment()
            val viewModel =
                ViewModelProvider(activity).get(AmitySingleReactionViewModel::class.java)

            viewModel.referenceType = referenceType
            viewModel.referenceId = referenceId

            if (userClickListener == null) {
                userClickListener = object : AmityUserClickListener {
                    override fun onClickUser(user: AmityUser) {
                        AmitySocialUISettings.globalUserClickListener.onClickUser(fragment, user)
                    }
                }
            }
            viewModel.userClickListener = userClickListener!!

            fragment.arguments = Bundle().apply {
                putString(REACTION_NAME, reactionName)
            }

            return fragment
        }

        internal fun referenceType(referenceType: AmityReactionReferenceType): Builder {
            this.referenceType = referenceType
            return this
        }

        internal fun referenceId(referenceId: String): Builder {
            this.referenceId = referenceId
            return this
        }

        internal fun reactionName(reactionName: String?): Builder {
            this.reactionName = reactionName
            return this
        }
    }

    companion object {

        fun newInstance(
            referenceType: AmityReactionReferenceType,
            referenceId: String,
            reactionName: String?
        ): Builder {
            return Builder()
                .referenceType(referenceType)
                .referenceId(referenceId)
                .reactionName(reactionName)
        }
    }
}

private const val REACTION_NAME = "REACTION_NAME"
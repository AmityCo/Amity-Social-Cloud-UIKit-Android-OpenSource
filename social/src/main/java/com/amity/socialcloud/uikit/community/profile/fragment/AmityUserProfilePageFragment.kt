package com.amity.socialcloud.uikit.community.profile.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.setSafeOnClickListener
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentUserProfilePageBinding
import com.amity.socialcloud.uikit.community.followers.AmityUserFollowersActivity
import com.amity.socialcloud.uikit.community.followrequest.AmityFollowRequestsActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityLiveStreamPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPollPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostDetailsActivity
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityMediaGalleryFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityMyFeedFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityUserFeedFragment
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityMediaGalleryTarget
import com.amity.socialcloud.uikit.community.profile.activity.AmityEditUserProfileActivity
import com.amity.socialcloud.uikit.community.profile.listener.AmityEditUserProfileClickListener
import com.amity.socialcloud.uikit.community.profile.listener.AmityFeedFragmentDelegate
import com.amity.socialcloud.uikit.community.profile.viewmodel.AmityUserProfileViewModel
import com.amity.socialcloud.uikit.community.setting.user.AmityUserSettingsActivity
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

const val ARG_USER_ID = "ARG_USER_ID"

class AmityUserProfilePageFragment : AmityBaseFragment(),
    AppBarLayout.OnOffsetChangedListener {

    private var TAG = AmityUserProfilePageFragment::class.java.canonicalName

    lateinit var viewModel: AmityUserProfileViewModel
    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private lateinit var binding: AmityFragmentUserProfilePageBinding

    private var isRefreshing = false
    private lateinit var currentUser: AmityUser
    private var refreshEventPublisher = PublishSubject.create<AmityFeedRefreshEvent>()

    private val createGenericPost =
        registerForActivityResult(AmityPostCreatorActivity.AmityCreateCommunityPostActivityContract()) {
        }

    private val createLiveStreamPost =
        registerForActivityResult(AmityLiveStreamPostCreatorActivity.AmityCreateLiveStreamPostActivityContract()) {
            it?.let {
                val intent = AmityPostDetailsActivity.newIntent(requireContext(), it, null, null)
                startActivity(intent)
            }
        }

    private val createPollPost =
        registerForActivityResult(AmityPollPostCreatorActivity.AmityPollCreatorActivityContract()) {
        }

    companion object {
        fun newInstance(userId: String): Builder {
            return Builder().userId(userId)
        }

        fun newInstance(user: AmityUser): Builder {
            return Builder().user(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(AmityUserProfileViewModel::class.java)
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.amity_fragment_user_profile_page,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        binding.appBar.setExpanded(true)
        getUserDetails()
        binding.fabCreatePost.setSafeOnClickListener {
            navigateToCreatePost()
        }
        setHeaderViewClickListeners()

        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refreshFeed()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isRefreshing) {
            binding.refreshLayout?.isRefreshing = true
            refreshFeed()
        }
        isRefreshing = true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        binding.refreshLayout.isEnabled = (verticalOffset == 0)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding.appBar.removeOnOffsetChangedListener(this)
    }

    private fun navigateToCreatePost() {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        val postCreationOptions =
            arrayListOf(
                BottomSheetMenuItem(
                    iconResId = R.drawable.amity_ic_post_create,
                    titleResId = R.string.amity_post,
                    action = {
                        createGenericPost.launch(null)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_live_stream_create,
                    titleResId = R.string.amity_video_stream_title,
                    action = {
                        createLiveStreamPost.launch(null)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_poll_create,
                    titleResId = R.string.amity_general_poll,
                    action = {
                        createPollPost.launch(null)
                        bottomSheet.dismiss()
                    }
                )
            )

        bottomSheet.show(postCreationOptions)
    }

    private fun refreshFeed() {
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is AmityMyFeedFragment -> {
                    refreshEventPublisher.onNext(AmityFeedRefreshEvent())
                }

                is AmityUserFeedFragment -> {
                    refreshEventPublisher.onNext(AmityFeedRefreshEvent())
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.refreshLayout.isRefreshing = false
        }, 1000)
        getFollowInfo()
    }

    private fun setHeaderViewClickListeners() {
        binding.userProfileHeader.getHeaderBinding().apply {
            btnProfileDefaultAction.setOnClickListener {
                if (viewModel.editUserProfileClickListener != null) {
                    viewModel.editUserProfileClickListener?.onClickEditUserProfile(viewModel.userId)
                } else {
                    if (viewModel.isSelfUser()) {
                        activity?.also {
                            val intent = AmityEditUserProfileActivity.newIntent(it)
                            startActivity(intent)
                        }
                    }
                }
            }

            btnFollow.setOnClickListener {
                binding.userProfileHeader.updateState(AmityFollowStatus.PENDING)
                viewModel.sendFollowRequest(onSuccess = {
                    binding.userProfileHeader.updateState(it)
                }, onError = {
                    showErrorDialog(
                        getString(R.string.amity_follow_error, currentUser.getDisplayName()),
                        getString(R.string.amity_something_went_wrong_pls_try),
                        AmityFollowStatus.NONE
                    )
                }).untilLifecycleEnd(this@AmityUserProfilePageFragment)
                    .subscribe()
            }

            btnCancelRequest.setOnClickListener {
                binding.userProfileHeader.updateState(AmityFollowStatus.NONE)
                viewModel.unFollow().doOnError {
                    showErrorDialog(
                        getString(R.string.amity_unfollow_error, currentUser.getDisplayName()),
                        getString(R.string.amity_something_went_wrong_pls_try),
                        AmityFollowStatus.PENDING
                    )
                }.untilLifecycleEnd(this@AmityUserProfilePageFragment)
                    .subscribe()
            }

            btnUnblockRequest.setOnClickListener {
                binding.userProfileHeader.updateState(AmityFollowStatus.NONE)
                viewModel.unblock().doOnError {
                    showErrorDialog(
                        getString(R.string.amity_unblock_error, currentUser.getDisplayName()),
                        getString(R.string.amity_something_went_wrong_pls_try),
                        AmityFollowStatus.BLOCKED
                    )
                }.untilLifecycleEnd(this@AmityUserProfilePageFragment)
                    .subscribe()
            }

            layoutPendingRequests.setOnClickListener {
                val intent =
                    AmityFollowRequestsActivity.newIntent(requireContext(), viewModel.userId)
                startActivity(intent)
            }

            tvFollowersCount.setOnClickListener {
                if (followStatus == AmityFollowStatus.ACCEPTED) {
                    val intent = AmityUserFollowersActivity.newIntent(
                        requireContext(),
                        currentUser.getDisplayName(),
                        viewModel.userId
                    )
                    startActivity(intent)
                }
            }

            tvFollowingCount.setOnClickListener {
                if (followStatus == AmityFollowStatus.ACCEPTED) {
                    val intent = AmityUserFollowersActivity.newIntent(
                        requireContext(),
                        currentUser.getDisplayName(),
                        viewModel.userId
                    )
                    startActivity(intent)
                }
            }
        }
    }

    private fun getUserDetails() {
        viewModel.getUser().let {
            disposable.add(
                it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        binding.userProfileHeader.setUserData(result)
                        currentUser = result
                        binding.fabCreatePost.visibility =
                            if (viewModel.isSelfUser()) View.VISIBLE else View.GONE
                    }, {
                        Timber.d(TAG, it.message)
                    })
            )
        }
        getFollowInfo()
    }

    private fun getFollowInfo() {
        if (viewModel.isSelfUser()) {
            viewModel.getMyFollowInfo(
                onSuccess = {
                    binding.userProfileHeader.setMyFollowInfo(it)
                },
                onError = {
                    Timber.e(TAG, "getMyFollowInfo: ${it.localizedMessage}")
                }
            ).untilLifecycleEnd(this)
                .subscribe()
        } else {
            viewModel.getUserFollowInfo(
                onSuccess = {
                    binding.userProfileHeader.setUserFollowInfo(it)
                },
                onError = {
                    Timber.e(TAG, "getUserFollowInfo: ${it.localizedMessage}")
                }
            ).untilLifecycleEnd(this)
                .subscribe()
        }
    }

    private fun initTabLayout() {
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_timeline),
                    getTimeLineFragment()
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_gallery_title),
                    getPostGalleryFragment()
                )
            )
        )
        binding.tabLayout.setAdapter(fragmentStateAdapter)
    }

    private fun getPostGalleryFragment(): Fragment {
        return AmityMediaGalleryFragment.newInstance()
            .build(AmityMediaGalleryTarget.USER(viewModel.userId))
    }

    private fun getTimeLineFragment(): Fragment {
        if (viewModel.feedFragmentDelegate != null)
            return viewModel.feedFragmentDelegate!!.getFeedFragment()
        return if (viewModel.isSelfUser()) {
            AmityMyFeedFragment.newInstance()
                .feedRefreshEvents(refreshEventPublisher.toFlowable(BackpressureStrategy.BUFFER))
                .build(requireActivity() as AppCompatActivity)
        } else {
            AmityUserFeedFragment.newInstance(viewModel.userId)
                .feedRefreshEvents(refreshEventPublisher.toFlowable(BackpressureStrategy.BUFFER))
                .build(activity as AppCompatActivity)
        }
    }

    private fun showErrorDialog(title: String, description: String, prevState: AmityFollowStatus) {
        AmityAlertDialogUtil.showDialog(requireContext(), title, description,
            getString(R.string.amity_ok), null,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    dialog.cancel()
                    binding.userProfileHeader.updateState(prevState)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_more_horiz)
        drawable?.mutate()
        drawable?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            R.color.amityColorBlack, BlendModeCompat.SRC_ATOP
        )
        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.amity_more_options))
            ?.setIcon(drawable)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = AmityUserSettingsActivity.newIntent(requireContext(), currentUser)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    class Builder internal constructor() {
        private var userId: String = ""
        var feedFragmentDelegate: AmityFeedFragmentDelegate? = null
        var editUserProfileClickListener: AmityEditUserProfileClickListener? = null

        fun build(activity: AppCompatActivity): AmityUserProfilePageFragment {
            val fragment = AmityUserProfilePageFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityUserProfileViewModel::class.java)
            fragment.viewModel.feedFragmentDelegate = feedFragmentDelegate
            fragment.viewModel.editUserProfileClickListener = editUserProfileClickListener
            fragment.viewModel.userId = userId
            return fragment
        }

        @Deprecated("FragmentDelegate will not be in use")
        fun feedFragmentDelegate(delegate: AmityFeedFragmentDelegate): Builder {
            this.feedFragmentDelegate = delegate
            return this
        }

        private fun onClickEditUserProfile(onEditUserProfileClickListener: AmityEditUserProfileClickListener): Builder {
            return apply { this.editUserProfileClickListener = onEditUserProfileClickListener }
        }

        internal fun userId(userId: String): Builder {
            this.userId = userId
            return this
        }

        internal fun user(user: AmityUser): Builder {
            this.userId = user.getUserId()
            return this
        }
    }
}
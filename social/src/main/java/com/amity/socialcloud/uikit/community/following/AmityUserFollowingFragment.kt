package com.amity.socialcloud.uikit.community.following

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.amity_user_following_fragment.*
import java.util.concurrent.TimeUnit

class AmityUserFollowingFragment :
    RxFragment(R.layout.amity_user_following_fragment) {

    lateinit var viewModel: AmityUserFollowingViewModel
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    private lateinit var followingAdapter: AmityFollowingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AmityUserFollowingViewModel::class.java)
        setUpRecyclerView()
        subscribeSearchStringChange()
        refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setUpRecyclerView() {
        val userClickListener = object: AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(this@AmityUserFollowingFragment, user)
            }
        }
        followingAdapter = AmityFollowingAdapter(requireContext(), userClickListener)
        rvUserFollowing.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        getFollowing()
    }

    private fun getFollowing() {
        viewModel.getFollowingList {
            refreshLayout.isRefreshing = false
            followingAdapter.submitList(it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    private fun refresh() {
        refreshLayout.isRefreshing = true
        getFollowing()
    }

    private fun subscribeSearchStringChange() {
        etSearch.doAfterTextChanged {
            if (it != null) {
                textChangeSubject.onNext(it.toString())
            }
        }
        textChangeDisposable = textChangeSubject.debounce(300, TimeUnit.MILLISECONDS)
            .map {
                if (it.isEmpty()) {
                    getFollowing()
                } else {
                    searchFollowing(it)
                }
            }
            .subscribe()
    }

    private fun searchFollowing(input: String) {

    }

    override fun onDestroy() {
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
        super.onDestroy()
    }

    class Builder internal constructor() {
        var userId: String = ""

        fun build(activity: AppCompatActivity): AmityUserFollowingFragment {
            val fragment = AmityUserFollowingFragment()
            fragment.viewModel = ViewModelProvider(activity).get(AmityUserFollowingViewModel::class.java)
            fragment.viewModel.userId = userId
            return fragment
        }

        internal fun setUserId(userId: String): Builder {
            this.userId =  userId
            return this
        }
    }

    companion object {

        fun newInstance(userId: String): Builder {
            return Builder().setUserId(userId)
        }
    }
}
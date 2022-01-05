package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostItemListener
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostContentItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostHeaderItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val SAVED_POST_ID = "SAVED_POST_ID"
class AmitySinglePostViewModel(private val savedState: SavedStateHandle) : AmityFeedViewModel() {


    lateinit var postItemListener: AmityPostItemListener
    var postId: String = ""
        set(value) {
            savedState.set(SAVED_POST_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_POST_ID)?.let { postId = it }
    }

    @ExperimentalPagingApi
    override fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit): Completable {
        val feedRepository = AmitySocialClient.newFeedRepository()
        return feedRepository.getPost(postId)
            .subscribeOn(Schedulers.io())
            .map {
                createPostItem(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onPageLoaded.invoke(PagingData.from(listOf(it)))
            }
            .ignoreElements()
    }

    override fun createPostHeaderItems(post: AmityPost): List<AmityBasePostHeaderItem> {
        val showHeader = AmitySocialUISettings.getViewHolder(getDataType(post)).enableHeader()
        return if (showHeader) {
            listOf(AmityBasePostHeaderItem(post = post, showTarget = true, showOptions = false))
        } else {
            emptyList()
        }
    }

    override fun createPostContentItems(post: AmityPost): List<AmityBasePostContentItem> {
        return listOf(AmityBasePostContentItem(post, true))
    }

    override fun createPostFooterItems(post: AmityPost): List<AmityBasePostFooterItem> {
        val footerItems = mutableListOf<AmityBasePostFooterItem>()
        val showFooter = AmitySocialUISettings.getViewHolder(getDataType(post)).enableFooter()
        if(post.getFeedType() == AmityFeedType.REVIEWING) {
            if(checkReviewPermission(post).blockingFirst()) {
                val review = AmityBasePostFooterItem.POST_REVIEW(post)
                footerItems.add(review)
            }
        }
        else if (showFooter) {
            val isReadOnly = isPostReadOnly(post)
            val engagement = AmityBasePostFooterItem.POST_ENGAGEMENT(post, isReadOnly)
            footerItems.add(engagement)
        }
        return footerItems
    }
    
    private fun checkReviewPermission(post: AmityPost): Flowable<Boolean> {
            val target = post.getTarget()
            if (target is AmityPost.Target.COMMUNITY && target.getCommunity() != null) {
                val community = target.getCommunity()!!
                return hasCommunityPermission(Flowable.just(community) , getCommunityPermissionSource(community.getCommunityId(), AmityPermission.REVIEW_COMMUNITY_POST))
            } else {
                return Flowable.just(false)
            }
    }

}
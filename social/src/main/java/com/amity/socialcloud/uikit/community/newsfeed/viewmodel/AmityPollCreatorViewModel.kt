package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.AmityUserRepository
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityPollCreatorViewModel : AmityBaseViewModel() {

    var communityId: String? = null
    var community: AmityCommunity? = null
    private val userRepository: AmityUserRepository = AmityCoreClient.newUserRepository()
    private val communityRepository = AmitySocialClient.newCommunityRepository()

    fun createPoll(
        question: String,
        answerType: AmityPoll.AnswerType,
        answers: List<AmityPollAnswer.Data>,
        closedIn: Long,
        userMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        return AmitySocialClient.newPollRepository()
            .createPoll(question = question)
            .answerType(answerType = answerType)
            .answers(answers = answers)
            .closedIn(closedIn = closedIn)
            .build()
            .create()
            .flatMapCompletable { poll ->
                AmitySocialClient.newPostRepository()
                    .createPost()
                    .run {
                        communityId?.let { communityId ->
                            targetCommunity(communityId)
                        } ?: run {
                            targetMe()
                        }
                    }
                    .poll(poll)
                    .text(question)
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
                    .build()
                    .post()
                    .ignoreElement()
            }
    }

    @ExperimentalPagingApi
    fun searchCommunityUsersMention(
        communityId: String,
        keyword: String,
        onResult: (users: PagingData<AmityCommunityMember>) -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityCommunityMembership.MEMBER))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    fun searchUsersMention(
        keyword: String,
        onResult: (users: PagingData<AmityUser>) -> Unit
    ): Completable {
        return userRepository.searchUsers(keyword)
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    fun observeCommunity(communityId: String?): Completable {
        this.communityId = communityId
        return communityId?.let {
            communityRepository.getCommunity(communityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    this.community = it
                }
                .ignoreElements()
        } ?: Completable.complete()
    }
}
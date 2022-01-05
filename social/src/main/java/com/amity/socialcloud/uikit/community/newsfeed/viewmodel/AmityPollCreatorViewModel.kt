package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPoll
import com.amity.socialcloud.sdk.social.feed.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable

class AmityPollCreatorViewModel : AmityBaseViewModel() {

    var communityId: String? = null

    fun createPoll(
        question: String,
        answerType: AmityPoll.AnswerType,
        answers: List<AmityPollAnswer.Data>,
        closedIn: Long
    ): Completable {
        return AmitySocialClient.newPollRepository()
            .createPoll(question = question)
            .answerType(answerType = answerType)
            .answers(answers = answers)
            .closedIn(closedIn = closedIn)
            .build()
            .create()
            .flatMapCompletable {
                AmitySocialClient.newPostRepository()
                    .createPost()
                    .run {
                        communityId?.let {
                            targetCommunity(it)
                        } ?: run {
                            targetMe()
                        }
                    }
                    .poll(it)
                    .build()
                    .post()
                    .ignoreElement()
            }
    }
}
package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class AmityPollPostComposerViewModel : AmityBaseViewModel() {

    fun createPost(
        question: String,
        options: List<String>,
        isMultipleChoice: Boolean,
        duration: Long,
        targetId: String,
        targetType: AmityPost.TargetType,
        mentionedUsers: List<AmityMentionMetadata.USER>,
    ) : Single<AmityPost> {
        val target = when (targetType) {
            AmityPost.TargetType.COMMUNITY -> AmityPost.Target.COMMUNITY.create(targetId)
            AmityPost.TargetType.USER -> AmityPost.Target.USER.create(targetId)
            else -> AmityPost.Target.UNKNOWN
        }
        val metadata = mentionedUsers.takeIf { it.isNotEmpty() }?.let {
            AmityMentionMetadataCreator(mentionedUsers).create()
        }
        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

        return createPollPost(
            question = question,
            options = options,
            isMultipleChoice = isMultipleChoice,
            closedIn = duration,
            target = target,
            metadata = metadata,
            mentionUserIds = mentionUserIds
        )
    }

    private fun createPollPost(
        question: String,
        options: List<String>,
        isMultipleChoice: Boolean,
        closedIn: Long,
        target: AmityPost.Target,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
    ): Single<AmityPost> {
        val pollRepository = AmitySocialClient.newPollRepository()
        val postRepository = AmitySocialClient.newPostRepository()
        return pollRepository.createPoll(
            question = question,
        )
            .answers(options.map { option -> AmityPollAnswer.Data.TEXT(option) })
            .closedIn(closedIn)
            .apply {
                if (isMultipleChoice) {
                    answerType(AmityPoll.AnswerType.MULTIPLE)
                } else {
                    answerType(AmityPoll.AnswerType.SINGLE)
                }
            }
            .build()
            .create()
            .flatMap {
                postRepository
                    .createPollPost(
                        target = target,
                        pollId = it,
                        text = question,
                        metadata = metadata,
                        mentionUserIds = mentionUserIds,
                    )
            }
    }

}
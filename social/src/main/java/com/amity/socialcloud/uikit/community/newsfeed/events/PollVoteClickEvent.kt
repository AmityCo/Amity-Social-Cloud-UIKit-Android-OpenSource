package com.amity.socialcloud.uikit.community.newsfeed.events

sealed class PollVoteClickEvent(val pollId: String, val answerIds: List<String>) {

    class VOTE(pollId: String, answerIds: List<String>) : PollVoteClickEvent(pollId, answerIds)
}
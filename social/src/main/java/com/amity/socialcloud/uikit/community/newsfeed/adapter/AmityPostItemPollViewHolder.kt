package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPoll
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPollPostBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PollVoteClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.card.MaterialCardView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import kotlin.math.min

class AmityPostItemPollViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    override fun bind(post: AmityPost) {
        val context = itemView.context
        val binding = AmityItemPollPostBinding.bind(itemView)

        val data = post.getChildren()[0].getData() as AmityPost.Data.POLL
        data.getPoll()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                binding.questionTextView.text = it.getQuestion()
                if (showFullContent) {
                    binding.questionTextView.showCompleteText()
                    binding.questionTextView.setOnClickListener(null)
                } else {
                    binding.questionTextView.setOnClickListener {
                        binding.questionTextView.showCompleteText()
                        binding.questionTextView.setOnClickListener(null)
                    }
                }
            }
            .doOnNext {
                when {
                    AmityPoll.Status.OPEN == it.getStatus() -> {
                        when {
                            Days.daysBetween(DateTime.now(), it.getClosedAt()).days > 0 -> {
                                val days = Days.daysBetween(DateTime.now(), it.getClosedAt()).days
                                binding.statusTextView.text =
                                    context.resources.getQuantityString(R.plurals.amity_poll_status_closed_in_days, days, days)
                            }
                            Hours.hoursBetween(DateTime.now(), it.getClosedAt()).hours > 0 -> {
                                val hours = Hours.hoursBetween(DateTime.now(), it.getClosedAt()).hours
                                binding.statusTextView.text =
                                    context.resources.getQuantityString(R.plurals.amity_poll_status_closed_in_hours, hours, hours)
                            }
                            else -> {
                                val minutes = min(1, Minutes.minutesBetween(DateTime.now(), it.getClosedAt()).minutes)
                                binding.statusTextView.text =
                                    context.resources.getQuantityString(R.plurals.amity_poll_status_closed_in_minutes, minutes, minutes)
                            }
                        }
                    }
                    AmityPoll.Status.CLOSED == it.getStatus() -> {
                        binding.statusTextView.text = context.getString(R.string.amity_poll_status_closed)
                    }
                }
            }
            .doOnNext {
                var totalVoteCount = 0
                it.getAnswers().forEach { answer -> totalVoteCount += answer.voteCount }
                binding.voteCountTextView.text = context.resources.getQuantityString(R.plurals.amity_poll_vote_count, totalVoteCount, totalVoteCount)

                binding.pollRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.pollRecyclerView.setHasFixedSize(true)
                binding.pollRecyclerView.setItemViewCacheSize(10)

                when (it.isVoted() || it.getStatus() == AmityPoll.Status.CLOSED) {
                    true -> {
                        binding.submitTextView.isVisible = false
                        binding.submitTextView.setOnClickListener(null)

                        val adapter = AmityPollAnswerVotedAdapter(
                            totalVoteCount = totalVoteCount,
                            showFullContent = showFullContent
                        )

                        adapter.setHasStableIds(true)
                        binding.pollRecyclerView.adapter = adapter
                        adapter.submitList(listItems = it.getAnswers())
                    }
                    false -> {
                        val target = post.getTarget()
                        val isEnabled = when (target is AmityPost.Target.COMMUNITY) {
                            true -> target.getCommunity()?.isJoined() ?: false && post.getFeedType() == AmityFeedType.PUBLISHED
                            false -> true
                        }

                        binding.submitTextView.isVisible = it.getStatus() == AmityPoll.Status.OPEN && isEnabled
                        binding.submitTextView.setOnClickListener(null)
                        binding.submitTextView.setTextColor(ContextCompat.getColor(context, R.color.amityColorShuttleGray))

                        val answerIds = hashSetOf<String>()
                        val holders = mutableMapOf<String, MaterialCardView>()
                        val adapter = AmityPollAnswerAdapter(
                            answerType = it.getAnswerType(),
                            showFullContent = showFullContent,
                            isEnabled = isEnabled
                        ) { answerId, isSelected, cardView ->
                            if (isSelected) {
                                if (it.getAnswerType() == AmityPoll.AnswerType.SINGLE) {
                                    holders.values.forEach { holder ->
                                        holder.performClick()
                                        holder.invalidate()
                                    }
                                }

                                answerIds.add(answerId)
                                cardView?.let { card ->
                                    holders.put(answerId, card)
                                }
                            } else {
                                answerIds.remove(answerId)
                                holders.remove(answerId)
                            }

                            val isVotable = when (it.getAnswerType()) {
                                AmityPoll.AnswerType.SINGLE -> answerIds.size == 1
                                else -> answerIds.size > 0
                            }

                            binding.submitTextView.setOnClickListener(
                                when (isVotable) {
                                    true -> View.OnClickListener { _ ->
                                        val event = PollVoteClickEvent.VOTE(pollId = it.getPollId(), answerIds = answerIds.toList())
                                        pollVoteClickPublisher.onNext(event)
                                    }
                                    false -> null
                                })

                            binding.submitTextView.setTextColor(
                                when (isVotable) {
                                    true -> ContextCompat.getColor(context, R.color.amityColorPrimary)
                                    false -> ContextCompat.getColor(context, R.color.amityColorShuttleGray)
                                }
                            )
                        }

                        adapter.setHasStableIds(true)
                        binding.pollRecyclerView.adapter = adapter
                        adapter.submitList(listItems = it.getAnswers())
                    }
                }
            }
            .doOnNext {
                if (!showFullContent && it.getAnswers().size > 2) {
                    binding.expandTextView.text = when (it.isVoted()) {
                        true -> context.getString(R.string.amity_poll_expand_voted)
                        false -> context.resources.getQuantityString(R.plurals.amity_poll_expand, it.getAnswers().size - 2, it.getAnswers().size - 2)
                    }

                    binding.expandTextView.isVisible = true
                    binding.expandTextView.setOnClickListener { postContentClickPublisher.onNext(PostContentClickEvent.Text(post)) }
                } else {
                    binding.expandTextView.isVisible = false
                    binding.expandTextView.setOnClickListener(null)
                }
            }
            .subscribeOn(Schedulers.io())
            .untilLifecycleEnd(itemView)
            .subscribe()
    }
}
package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.social.feed.AmityPoll
import com.amity.socialcloud.sdk.social.feed.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentPollCreatorBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPollDraftAnswerAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionPagingDataAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityPollCreatorViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.Days

private const val DEFAULT_TIME_FRAME_DAYS = 30
private const val MAX_QUESTION_LENGTH = 500
private const val MIN_ANSWER_COUNT = 2
private const val MAX_ANSWER_COUNT = 10

internal const val MAX_ANSWER_LENGTH = 200

class AmityPollPostCreatorFragment : AmityBaseFragment(), SuggestionsVisibilityManager, QueryTokenReceiver {

    private lateinit var binding: AmityFragmentPollCreatorBinding
    private lateinit var adapter: AmityPollDraftAnswerAdapter
    private val viewModel: AmityPollCreatorViewModel by viewModels()
    private val userMentionAdapter by lazy { AmityUserMentionAdapter() }
    private val userMentionPagingDataAdapter by lazy { AmityUserMentionPagingDataAdapter() }
    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = AmityFragmentPollCreatorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        highlightLastChar(binding.questionTitleTextView)
        highlightLastChar(binding.answerTitleTextView)

        initQuestion()
        initAnswers()
        initTimeFrame()
        setupUserMention()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(EXTRA_PARAM_COMMUNITY_ID).let {
            viewModel.observeCommunity(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .untilLifecycleEnd(this)
                    .subscribe()
        }
    }

    private fun highlightLastChar(textView: TextView) {
        val spannableString = SpannableString(textView.text)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.amityColorAlert)),
            textView.length() - 1,
            textView.length(),
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun initQuestion() {
        updateQuestion()

        binding.questionEditText.doAfterTextChanged {
            updateQuestion(it)
        }

        binding.questionEditText.requestFocus()
        AmityAndroidUtil.showKeyboard(binding.questionEditText)
    }

    private fun updateQuestion(it: Editable? = null) {
        binding.questionCountTextView.text =
            String.format("%s/%s", it?.length ?: 0, MAX_QUESTION_LENGTH)

        val isExceeded = binding.questionEditText.length() > MAX_QUESTION_LENGTH

        binding.questionCountTextView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                when (isExceeded) {
                    true -> R.color.amityColorAlert
                    false -> R.color.amityColorShuttleGray
                }
            )
        )

        binding.questionErrorTextView.isVisible = isExceeded
        binding.questionDivider.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                when (isExceeded) {
                    true -> R.color.amityColorAlert
                    false -> R.color.amityColorAthensGray
                }
            )
        )

        requireActivity().invalidateOptionsMenu()
    }

    private fun initAnswers() {
        updateAnswers()

        adapter = AmityPollDraftAnswerAdapter {
            adapter.removeAnswer(answer = it)
            updateAnswers(adapter.itemCount)
        }

        adapter.setHasStableIds(true)

        binding.answerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.answerRecyclerView.itemAnimator = null
        binding.answerRecyclerView.setItemViewCacheSize(10)
        binding.answerRecyclerView.adapter = adapter

        binding.addAnswerLinearLayout.setOnClickListener {
            adapter.addAnswer()
            updateAnswers(adapter.itemCount)
        }
    }

    private fun updateAnswers(count: Int = 0) {
        binding.answerCountTextView.text = String.format("%s/%s", count, MAX_ANSWER_COUNT)
        binding.addAnswerLinearLayout.isVisible = count < MAX_ANSWER_COUNT

        requireActivity().invalidateOptionsMenu()
    }

    private fun initTimeFrame() {
        updateTimeFrame()

        binding.closedInEditText.doAfterTextChanged {
            updateTimeFrame()
        }
    }

    private fun updateTimeFrame() {
        val isExceeded =
            binding.closedInEditText.text?.toString()?.toIntOrNull() ?: 0 > DEFAULT_TIME_FRAME_DAYS

        binding.closedInErrorTextView.isVisible = isExceeded
        binding.closedInDivider.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                when (isExceeded) {
                    true -> R.color.amityColorAlert
                    false -> R.color.amityColorAthensGray
                }
            )
        )

        requireActivity().invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem =
            menu.add(Menu.NONE, R.id.amity_poll_post, Menu.NONE, getString(R.string.amity_post))

        val isEnabled = binding.questionEditText.length() in 1..MAX_QUESTION_LENGTH
                && adapter.itemCount >= MIN_ANSWER_COUNT
                && binding.closedInEditText.text?.toString()
            ?.toIntOrNull() ?: 0 <= DEFAULT_TIME_FRAME_DAYS

        val color = when (isEnabled) {
            true -> ContextCompat.getColor(requireContext(), R.color.amityColorPrimary)
            false -> ContextCompat.getColor(requireContext(), R.color.amityColorShuttleGray)
        }

        val title = SpannableString(menuItem.title)
        title.setSpan(ForegroundColorSpan(color), 0, title.length, 0)

        menuItem.title = title
        menuItem.isEnabled = isEnabled
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.amity_poll_post) {
            for (answer in adapter.getItems()) {
                if (answer.data.isEmpty() || answer.data.length > MAX_ANSWER_LENGTH) {
                    adapter.invalidateAnswers()
                    return true
                }
            }

            viewModel.createPoll(
                question = binding.questionEditText.text.toString(),
                answerType = when (binding.multipleAnswerSwitch.isChecked) {
                    true -> AmityPoll.AnswerType.MULTIPLE
                    false -> AmityPoll.AnswerType.SINGLE
                },
                answers = adapter.getItems().map { AmityPollAnswer.Data.TEXT(it.data) },
                closedIn = Days.days(
                    binding.closedInEditText.text?.toString()?.toIntOrNull()
                        ?: DEFAULT_TIME_FRAME_DAYS
                ).toStandardDuration().millis,
                    binding.questionEditText.getUserMentions()
            )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    val resultIntent = Intent()
                    resultIntent.putExtra(
                        EXTRA_PARAM_POST_ID,
                        viewModel.communityId
                    )
                    activity?.setResult(Activity.RESULT_OK, resultIntent)
                    activity?.finish()
                }
                .untilLifecycleEnd(this)
                .subscribeOn(Schedulers.io())
                .subscribe()

            return true
        }
        return false
    }
    
    private fun setupUserMention() {
        binding.questionEditText.hint = resources.getString(R.string.amity_poll_question_hint)
        binding.questionEditText.apply {
            setSuggestionsVisibilityManager(this@AmityPollPostCreatorFragment)
            setQueryTokenReceiver(this@AmityPollPostCreatorFragment)
        }
        binding.recyclerViewUserMention.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUserMention.adapter = userMentionAdapter
        
        userMentionAdapter.setListener(object :
                AmityUserMentionAdapter.AmityUserMentionAdapterListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(userMention)
            }
        })
        
        userMentionPagingDataAdapter.setListener(object :
                AmityUserMentionViewHolder.AmityUserMentionListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(userMention)
            }
        })
    }
    
    private fun insertUserMention(userMention: AmityUserMention) {
        displaySuggestions(false)
        searchDisposable.clear()
        binding.questionEditText.insertMention(userMention)
    }
    
    override fun displaySuggestions(display: Boolean) {
        if (display) {
            binding.recyclerViewUserMention.visibility = View.VISIBLE
        } else {
            binding.recyclerViewUserMention.visibility = View.GONE
        }
    }
    
    override fun isDisplayingSuggestions(): Boolean {
        return binding.recyclerViewUserMention.visibility == View.VISIBLE
    }
    
    @ExperimentalPagingApi
    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        if (queryToken.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
            searchDisposable.clear()
            val disposable = if (viewModel.community?.isPublic() == false) {
                binding.recyclerViewUserMention.swapAdapter(userMentionPagingDataAdapter, true)
                viewModel.searchCommunityUsersMention(viewModel.community?.getCommunityId()!!,
                        queryToken.keywords, onResult = {
                    userMentionPagingDataAdapter.submitData(lifecycle, it)
                    displaySuggestions(true)
                }).subscribe()
            } else {
                binding.recyclerViewUserMention.swapAdapter(userMentionAdapter, true)
                viewModel.searchUsersMention(queryToken.keywords, onResult = {
                    userMentionAdapter.submitData(lifecycle, it)
                    displaySuggestions(true)
                }).subscribe()
            }
            searchDisposable.add(disposable)
        } else {
            displaySuggestions(false)
        }
        return mutableListOf()
    }

    class Builder internal constructor() {
        private var communityId: String? = null

        fun build(): AmityPollPostCreatorFragment {
            return AmityPollPostCreatorFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_COMMUNITY_ID, this@Builder.communityId)
                }
            }
        }

        internal fun onMyFeed(): Builder {
            return this
        }

        internal fun onCommunityFeed(communityId: String): Builder {
            this.communityId = communityId
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }
}
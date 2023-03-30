package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.common.utils.AmityOptionMenuColorUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentBaseCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionPagingDataAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCommentViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import io.reactivex.rxjava3.disposables.CompositeDisposable

const val ID_MENU_ITEM_COMMENT: Int = 144

abstract class AmityCommentBaseFragment: AmityBaseFragment(),
    AmityAlertDialogFragment.IAlertDialogActionListener,
    TextWatcher, SuggestionsVisibilityManager, QueryTokenReceiver {
    private val TAG = AmityCommentBaseFragment::class.java.canonicalName
    private var menuItemComment: MenuItem? = null
    lateinit var viewModel: AmityCommentViewModel
    lateinit var binding: AmityFragmentBaseCommentBinding

    private val userMentionAdapter by lazy { AmityUserMentionAdapter() }
    private val userMentionPagingDataAdapter by lazy { AmityUserMentionPagingDataAdapter() }

    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consumeBackPress = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.amity_fragment_base_comment,
                container,
                false
            )
        viewModel = ViewModelProvider(requireActivity()).get(AmityCommentViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        addEditCommentViewTextWatcher()
        if (viewModel.getReply() != null) {
            binding.replyingToUser = viewModel.getReply()?.getCreator()?.getDisplayName()
            binding.showReplyingTo = true
        }
        setupPost()
        setupUserMention()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItemComment = menu.add(Menu.NONE, ID_MENU_ITEM_COMMENT, Menu.NONE, getCommentMenuText())
        menuItemComment?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        updateCommentMenu(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    abstract fun getCommentMenuText(): String

    private fun setupPost() {
        viewModel.getPost(onResult = {
            viewModel.post = it
        })
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun setupUserMention() {
        binding.etPostComment.apply {
            setSuggestionsVisibilityManager(this@AmityCommentBaseFragment)
            addTextChangedListener(this@AmityCommentBaseFragment)
            setQueryTokenReceiver(this@AmityCommentBaseFragment)
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
        binding.etPostComment.insertMention(userMention)
    }

    private fun addEditCommentViewTextWatcher() {
        viewModel.commentText.observe(viewLifecycleOwner, Observer {
            viewModel.checkForCommentUpdate()
        })

        viewModel.hasCommentUpdate.observe(viewLifecycleOwner, Observer {
            updateCommentMenu(it)
        })
    }

    fun updateCommentMenu(enabled: Boolean) {
        if (menuItemComment != null) {
            menuItemComment?.isEnabled = enabled
            val s = SpannableString(menuItemComment?.title)
            s.setSpan(
                ForegroundColorSpan(
                    AmityOptionMenuColorUtil.getColor(
                        menuItemComment?.isEnabled ?: false,
                        requireContext()
                    )
                ), 0, s.length, 0
            )
            menuItemComment?.title = s
        }
    }

    override fun handleBackPress() {
        handleCancelPost()
    }

    private fun handleCancelPost() {
        if (viewModel.hasCommentUpdate.value!!) {
            showExitConfirmationDialog()
        } else {
            backPressFragment()
        }
    }

    private fun showExitConfirmationDialog() {
        val isReply = viewModel.getComment()?.getParentId()?.isNotEmpty() == true
        val exitConfirmationDialogFragment = if (viewModel.getReply() != null || isReply) {
            AmityAlertDialogFragment
                .newInstance(
                    R.string.amity_discard_reply_title, R.string.amity_discard_reply_message,
                    R.string.amity_discard, R.string.amity_cancel
                )
        } else {
            AmityAlertDialogFragment
                .newInstance(
                    R.string.amity_discard_comment_title, R.string.amity_discard_comment_message,
                    R.string.amity_discard, R.string.amity_cancel
                )
        }
        exitConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
        exitConfirmationDialogFragment.listener = this
    }

    override fun onClickPositiveButton() {
        backPressFragment()
    }

    override fun onClickNegativeButton() {
        Log.d(TAG, " Cancel discard comment")
    }

    @ExperimentalPagingApi
    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        if (queryToken.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
            searchDisposable.clear()
            val community = (viewModel.post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
            val disposable = if (community?.isPublic() == false) {
                binding.recyclerViewUserMention.swapAdapter(userMentionPagingDataAdapter, true)
                viewModel.searchCommunityUsersMention(community.getCommunityId(),
                    queryToken.keywords, onResult = {
                        userMentionPagingDataAdapter.submitData(lifecycle, it)
                        displaySuggestions(true)
                    })
                    .subscribe()
            } else {
                binding.recyclerViewUserMention.swapAdapter(userMentionAdapter, true)
                viewModel.searchUsersMention(queryToken.keywords, onResult = {
                    userMentionAdapter.submitData(lifecycle, it)
                    displaySuggestions(true)
                })
                    .subscribe()
            }
            searchDisposable.add(disposable)
        } else {
            displaySuggestions(false)
        }
        return mutableListOf()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        viewModel.commentText.value = binding.etPostComment.getTextCompose()
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
}

package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.expandViewHitArea
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentBasePostDetailBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityCommentCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostEditorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.EXTRA_PARAM_COMMENT_REPLY_TO
import com.amity.socialcloud.uikit.community.newsfeed.activity.EXTRA_PARAM_COMMENT_TEXT
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionPagingDataAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommentItemListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostItemListener
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityPostDetailViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.amity.types.ObjectId
import java.util.*

class AmityPostDetailFragment : AmityBaseFragment(),
    SuggestionsVisibilityManager, QueryTokenReceiver {

    private val ID_MENU_ITEM = Random().nextInt()
    private lateinit var binding: AmityFragmentBasePostDetailBinding
    private val viewModel: AmityPostDetailViewModel by viewModels()

    private var menuItem: MenuItem? = null
    private var replyTo: AmityComment? = null
    private var isFirstLoad = true

    private val userMentionAdapter by lazy { AmityUserMentionAdapter() }
    private val userMentionPagingDataAdapter by lazy { AmityUserMentionPagingDataAdapter() }

    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var postEditContact =
        registerForActivityResult(AmityPostEditorActivity.AmityEditPostActivityContract()) {
            // no need for this if update comes from Flowable
        }

    private var addCommentContract =
        registerForActivityResult(AmityCommentCreatorActivity.AmityAddCommentContract()) {
            if (it != null && it == true) {
                hideReplyTo()
                binding.commentComposeBar.getCommentEditText().setText("")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_base_post_detail,
            container,
            false
        )

        getInstanceState(savedInstanceState)
        return binding.root
    }

    private fun getInstanceState(savedInstanceState: Bundle?) {
        viewModel.postId = savedInstanceState?.getString(EXTRA_PARAM_POST_ID) ?: kotlin.run {
            arguments?.getString(EXTRA_PARAM_POST_ID) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observePost()
        renderPost()
        setupUserMention()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.post?.let {
            if (viewModel.shouldShowPostOptions(it)) {
                val drawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_more_horiz)
                drawable?.mutate()
                drawable?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    R.color.amityColorBlack,
                    BlendModeCompat.SRC_ATOP
                )
                menuItem = menu.add(
                    Menu.NONE,
                    ID_MENU_ITEM,
                    Menu.NONE,
                    getString(R.string.amity_cancel)
                )
                menuItem?.setIcon(drawable)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM) {
            showPostOptions()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_PARAM_POST_ID, viewModel.postId)
        super.onSaveInstanceState(outState)
    }

    private fun setupUserMention() {
        binding.commentComposeBar.getCommentEditText().apply {
            setSuggestionsVisibilityManager(this@AmityPostDetailFragment)
            setQueryTokenReceiver(this@AmityPostDetailFragment)
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
        binding.commentComposeBar.getCommentEditText().insertMention(userMention)
    }

    private fun observePost() {
        viewModel.getPost(postId = viewModel.postId, onLoaded = {
            viewModel.post = it
            this.requireActivity().invalidateOptionsMenu()
            if (it.isDeleted()) {
                backPressFragment()
            }
            renderFooter(it)
        }, onError = {})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun renderFooter(post: AmityPost) {
        if (!isFirstLoad) {
            return
        }
        isFirstLoad = false
        if (post.getFeedType() != AmityFeedType.REVIEWING
            && AmitySocialUISettings.getViewHolder(post.getType().getApiKey()).enableFooter()
        ) {
            binding.viewBottom.visibility = View.VISIBLE
            renderComments(post)
            setupComposeBar(post)
        } else {
            binding.viewBottom.visibility = View.GONE
        }
    }

    private fun renderPost() {
        val postItemListener = object : AmityPostItemListener {
            override fun onClickComment(post: AmityPost, fragment: Fragment) {
                if (isJoined(post)) {
                    replyTo = null
                    hideReplyTo()
                    binding.commentComposeBar.requestFocus()
                    AmityAndroidUtil.showKeyboard(binding.commentComposeBar.getCommentEditText())
                }
            }
        }

        val fragment = AmitySinglePostFragment.newInstance(viewModel.postId)
            .postItemListener(postItemListener)
            .build(requireActivity() as AppCompatActivity)

        childFragmentManager.beginTransaction()
            .replace(R.id.post_fragment_container, fragment)
            .commit()
    }

    private fun renderComments(post: AmityPost) {
        val commentItemListener = object : AmityCommentItemListener {
            override fun onClickReply(comment: AmityComment, fragment: Fragment) {
                replyTo = comment
                binding.replyingToUser = comment.getCreator()?.getDisplayName()
                showReplyTo()
            }
        }

        val fragment = AmityCommentListFragment.newInstance(post.getPostId())
            .readOnlyMode(viewModel.isPostReadOnly(post))
            .commentItemListener(commentItemListener)
            .build(requireActivity() as AppCompatActivity)

        childFragmentManager.beginTransaction()
            .replace(R.id.comment_list_fragment_container, fragment)
            .commit()
    }

    fun isJoined(post: AmityPost): Boolean {
        return (post.getTarget() as? AmityPost.Target.COMMUNITY)
            ?.getCommunity()?.isJoined() ?: true
    }

    private fun setupComposeBar(post: AmityPost) {
        binding.viewBottom.visibility = if (isJoined(post)) View.VISIBLE else View.INVISIBLE
        binding.commentComposeBar.getPostButton().setOnClickListener {
            val commentId = ObjectId.get().toHexString()
            viewModel.addComment(
                replyTo?.getCommentId(),
                commentId,
                post.getPostId(),
                binding.commentComposeBar.getTextCompose(),
                binding.commentComposeBar.getCommentEditText().getUserMentions(),
                onSuccess = {
                    binding.commentComposeBar.getCommentEditText().setText("")
                    hideReplyTo()
                },
                onError = {
                    // To do handle error
                },
                onBanned = {
                    binding.commentComposeBar.getCommentEditText().setText("")
                    hideReplyTo()
                    viewModel.deleteComment(commentId, onSuccess = {}, onError = {})
                        .untilLifecycleEnd(this)
                        .subscribe()
                })
                .untilLifecycleEnd(this)
                .subscribe()
        }

        binding.commentComposeBar.setCommentExpandClickListener {
            AmityAndroidUtil.hideKeyboard(binding.commentComposeBar.getCommentEditText())
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_COMMENT_REPLY_TO, replyTo)
            bundle.putString(EXTRA_PARAM_POST_ID, post.getPostId())
            bundle.putString(
                EXTRA_PARAM_COMMENT_TEXT,
                binding.commentComposeBar.getCommentEditText().getTextCompose()
            )
            addCommentContract.launch(bundle)
        }

        binding.imageviewCloseReply.setOnClickListener {
            hideReplyTo()
        }

        viewModel
            .getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                it.getAvatar()?.getUrl(AmityImage.Size.SMALL)?.let {
                    binding.commentComposeBar.setImageUrl(it)
                }
            }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showReplyTo() {
        binding.imageviewCloseReply.expandViewHitArea()
        binding.textviewReplyTo.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.amity_animation_fade_in
            )
        )
        binding.showReplying = true

        binding.commentComposeBar.requestFocus()
        AmityAndroidUtil.showKeyboard(binding.commentComposeBar.getCommentEditText())
    }

    private fun hideReplyTo() {
        replyTo = null
        binding.showReplying = false
        AmityAndroidUtil.hideKeyboard(binding.commentComposeBar.getCommentEditText())
        binding.commentComposeBar.getCommentEditText().clearFocus()
    }

    private fun showPostOptions() {
        viewModel.post?.let {
            val bottomSheet = AmityBottomSheetDialog(requireContext())
            bottomSheet.show(viewModel.getPostOptionMenuItems(
                post = it,
                editPost = {
                    bottomSheet.dismiss()
                    editPost(it)
                },
                deletePost = {
                    bottomSheet.dismiss()
                    showDeletePostWarning(it)
                },
                reportPost = {
                    bottomSheet.dismiss()
                    reportPost(it)
                },
                unReportPost = {
                    bottomSheet.dismiss()
                    unReportPost(it)
                },
                closePoll = {
                    bottomSheet.dismiss()
                    showClosePollWarning(it)
                },
                deletePoll = {
                    bottomSheet.dismiss()
                    showDeletePollWarning(it)
                }
            ))
        }
    }

    private fun editPost(post: AmityPost) {
        postEditContact.launch(post)
    }


    private fun reportPost(post: AmityPost) {
        viewModel.reportPost(post, {
            view?.showSnackBar(getString(R.string.amity_report_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun unReportPost(post: AmityPost) {
        viewModel.unReportPost(post, {
            view?.showSnackBar(getString(R.string.amity_unreport_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showDeletePostWarning(post: AmityPost) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_post_title,
                R.string.amity_delete_post_warning_message,
                R.string.amity_delete, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deletePost(post)
                }

                override fun onClickNegativeButton() {

                }
            }
    }

    private fun showClosePollWarning(post: AmityPost) {
        val closeConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_close_poll_title,
                R.string.amity_close_poll_message,
                R.string.amity_close, R.string.amity_cancel
            )
        closeConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        closeConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    val data = post.getChildren()[0].getData() as AmityPost.Data.POLL
                    closePoll(data.getPollId())
                }

                override fun onClickNegativeButton() {

                }
            }
    }

    private fun showDeletePollWarning(post: AmityPost) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_poll_title,
                R.string.amity_delete_poll_message,
                R.string.amity_delete_poll, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deletePost(post)
                }

                override fun onClickNegativeButton() {

                }
            }
    }

    private fun deletePost(post: AmityPost) {
        viewModel.deletePost(
            post = post,
            onSuccess = {
                backPressFragment()
            },
            onError = {

            }
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun closePoll(pollId: String) {
        viewModel.closePoll(
            pollId = pollId,
            onSuccess = {
                // do nothing
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    companion object {
        fun newInstance(postId: String): Builder {
            return Builder().postId(postId)
        }
    }

    class Builder internal constructor() {
        private lateinit var postId: String

        fun build(): AmityPostDetailFragment {
            return AmityPostDetailFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_PARAM_POST_ID, postId) }
            }
        }

        internal fun postId(postId: String): Builder {
            this.postId = postId
            return this
        }
    }

    @ExperimentalPagingApi
    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        if (queryToken.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
            searchDisposable.clear()
            val community =
                (viewModel.post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
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

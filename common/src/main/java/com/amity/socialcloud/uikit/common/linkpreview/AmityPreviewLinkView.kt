package com.amity.socialcloud.uikit.common.linkpreview

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.databinding.AmityViewLinkPreviewBinding
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataCacheItem
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataException
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewNoUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewUrlCacheItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityPreviewLinkView : ConstraintLayout {

    private lateinit var mBinding: AmityViewLinkPreviewBinding

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = AmityViewLinkPreviewBinding.inflate(inflater, this, true)
        updateUiState(UiState.Init)
    }

    fun loadPreview(post: AmityPost, postUrl: String?) {
        updateUiState(UiState.Init)
        val data = getPostPreviewUrl(post, postUrl)
        if (data != null && data !is AmityPreviewNoUrl) {
            //  if preview url is not empty and no url, show preview
            updateUiState(UiState.Loading)

            getPostPreviewMetadata(data.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateUiState(UiState.Success(it))
                }, {
                    updateUiState(UiState.FailedLoadMetadata(it as AmityPreviewMetadataException))
                }).isDisposed
        }
    }

    private fun loadPreviewImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    updateUiState(UiState.FailedLoadImage)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .centerCrop()
            .into(mBinding.ivPreviewImage)
    }

    private fun openBrowser(url: String) {
        if (url.isEmpty()) return

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    private fun getPostPreviewUrl(post: AmityPost, postUrl: String?): AmityPreviewUrlCacheItem? {
        val postId = post.getPostId()
        val editedAt = post.getEditedAt()

        return AmityPreviewUrl.getPostPreviewUrl(postId, postUrl, editedAt)
    }

    private fun getPostPreviewMetadata(url: String): Single<AmityPreviewMetadataCacheItem> {
        return AmityPreviewUrl.fetchMetadata(url)
    }

    private fun updateUiState(state: UiState) {
        when (state) {
            UiState.Init -> {
                mBinding.constraintLayout.visibility = View.GONE
            }

            UiState.Loading -> {
                mBinding.constraintLayout.visibility = View.VISIBLE
                mBinding.contentView.visibility = View.GONE
                mBinding.progressBar.visibility = View.VISIBLE
            }

            is UiState.Success,
            is UiState.FailedLoadMetadata -> {
                mBinding.constraintLayout.visibility = View.VISIBLE
                mBinding.contentView.visibility = View.VISIBLE
                mBinding.progressBar.visibility = View.GONE
            }

            else -> {}
        }

        handlePreviewContentUiState(state)
        handlePreviewImageUiState(state)
    }

    private fun handlePreviewContentUiState(state: UiState) {
        when (state) {
            is UiState.Success -> {
                state.metadata.let { metadata ->
                    mBinding.tvDomain.text = metadata.domain
                    mBinding.tvContent.text = metadata.title

                    mBinding.tvDomain.visibility = View.VISIBLE
                    mBinding.tvContent.visibility = View.VISIBLE
                    mBinding.tvErrorTitle.visibility = View.GONE
                    mBinding.tvErrorContent.visibility = View.GONE

                    mBinding.contentView.setOnClickListener {
                        openBrowser(metadata.url)
                    }
                }
            }

            is UiState.FailedLoadImage -> {}

            is UiState.FailedLoadMetadata -> {
                mBinding.tvDomain.visibility = View.GONE
                mBinding.tvContent.visibility = View.GONE
                mBinding.tvErrorTitle.visibility = View.VISIBLE
                mBinding.tvErrorContent.visibility = View.VISIBLE

                mBinding.contentView.setOnClickListener {
                    openBrowser(state.exception.url)
                }
            }

            else -> {
                mBinding.tvDomain.visibility = View.GONE
                mBinding.tvContent.visibility = View.GONE
                mBinding.tvErrorTitle.visibility = View.GONE
                mBinding.tvErrorContent.visibility = View.GONE

                mBinding.contentView.setOnClickListener(null)
            }
        }
    }

    private fun handlePreviewImageUiState(state: UiState) {
        when (state) {
            is UiState.Success -> {
                loadPreviewImage(state.metadata.imageUrl)

                mBinding.ivPreviewImage.visibility = View.VISIBLE
                mBinding.viewPreviewImagePlaceholder.visibility = View.GONE
                mBinding.ivPreviewImagePlaceholder.visibility = View.GONE
            }

            is UiState.FailedLoadImage -> {
                mBinding.ivPreviewImage.visibility = View.INVISIBLE

                mBinding.viewPreviewImagePlaceholder.visibility = View.VISIBLE
                mBinding.ivPreviewImagePlaceholder.visibility = View.VISIBLE
                mBinding.ivPreviewImagePlaceholder.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.amity_ic_placeholder_image)
                )
            }

            is UiState.FailedLoadMetadata -> {
                Glide.with(context).clear(mBinding.ivPreviewImage)
                mBinding.ivPreviewImage.visibility = View.INVISIBLE

                mBinding.viewPreviewImagePlaceholder.visibility = View.VISIBLE
                mBinding.ivPreviewImagePlaceholder.visibility = View.VISIBLE
                mBinding.ivPreviewImagePlaceholder.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.amity_ic_warning)
                )
            }

            else -> {
                Glide.with(context).clear(mBinding.ivPreviewImage)
                mBinding.ivPreviewImage.visibility = View.INVISIBLE
                mBinding.viewPreviewImagePlaceholder.visibility = View.GONE
                mBinding.ivPreviewImagePlaceholder.visibility = View.GONE
            }

        }
    }

    private sealed class UiState {
        object Init : UiState()
        object Loading : UiState()
        data class Success(val metadata: AmityPreviewMetadataCacheItem) : UiState()
        object FailedLoadImage : UiState()
        data class FailedLoadMetadata(val exception: AmityPreviewMetadataException) : UiState()
    }
}
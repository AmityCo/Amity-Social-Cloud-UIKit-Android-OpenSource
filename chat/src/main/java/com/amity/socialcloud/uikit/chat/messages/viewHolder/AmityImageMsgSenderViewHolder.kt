package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemImageMsgSenderBinding
import com.amity.socialcloud.uikit.chat.databinding.AmityPopupMsgDeleteBinding
import com.amity.socialcloud.uikit.chat.messages.popUp.AmityPopUp
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityImageMsgViewModel
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.imagepreview.AmityImagePreviewActivity
import com.amity.socialcloud.uikit.common.imagepreview.AmityPreviewImage
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.google.android.material.shape.CornerFamily

class AmityImageMsgSenderViewHolder(
    itemView: View,
    private val itemViewModel: AmityImageMsgViewModel,
    private val context: Context
) : AmitySelectableMessageViewHolder(itemView, itemViewModel, context) {

    private val binding: AmityItemImageMsgSenderBinding? = DataBindingUtil.bind(itemView)
    private var popUp: AmityPopUp? = null

    init {
        binding?.vmImageMessage = itemViewModel
        addViewModelListeners()
    }

    private fun addViewModelListeners() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.DISMISS_POPUP -> popUp?.dismiss()
                else -> {
                }
            }
        }
    }

    override fun setMessageData(item: AmityMessage) {
        itemViewModel.getImageUploadProgress(item)

        if (itemViewModel.imageUrl.get() != null && itemViewModel.imageUrl.get()!!
                .isNotEmptyOrBlank()
        ) {
            val radius = context.resources.getDimension(com.amity.socialcloud.uikit.common.R.dimen.amity_six)
            binding?.ivMsgOutgoing?.shapeAppearanceModel =
                binding?.ivMsgOutgoing?.shapeAppearanceModel
                    ?.toBuilder()
                    ?.setTopLeftCorner(CornerFamily.ROUNDED, radius)
                    ?.setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                    ?.setBottomRightCorner(CornerFamily.ROUNDED, radius)
                    ?.build()!!
        } else {
            binding?.ivMsgOutgoing?.setShape(
                null, null,
                itemView.context.resources.getDimension(R.dimen.amity_zero),
                null, R.color.amityColorBase, null, AmityColorShade.SHADE4
            )
        }

        binding?.ivMsgOutgoing?.setOnClickListener {
            itemViewModel.imageUrl.get()?.let {
                navigateToImagePreview(it)
            }
        }

        binding?.progressBar?.trackColor = AmityColorPaletteUtil.getColor(
            ContextCompat.getColor(context, R.color.amityColorBase),
            AmityColorShade.SHADE3
        )
    }

    private fun navigateToImagePreview(imageUrl: String) {
        val imageList = mutableListOf(AmityPreviewImage(imageUrl))
        val intent = AmityImagePreviewActivity.newIntent(context, 0, false, ArrayList(imageList))
        context.startActivity(intent)
    }

    override fun showPopUp() {
        if (!itemViewModel.uploading.get()) {
            popUp = AmityPopUp()
            val anchor: View = itemView.findViewById(R.id.ivMsgOutgoing)
            val inflater: LayoutInflater = LayoutInflater.from(anchor.context)
            val binding: AmityPopupMsgDeleteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.amity_popup_msg_delete, null, true
            )
            binding.viewModel = itemViewModel
            popUp?.showPopUp(binding.root, anchor, itemViewModel, AmityPopUp.PopUpGravity.END)
        }
    }
}
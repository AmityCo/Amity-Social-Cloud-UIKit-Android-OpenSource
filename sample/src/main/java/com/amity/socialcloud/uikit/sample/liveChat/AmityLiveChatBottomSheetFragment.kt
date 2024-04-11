package com.amity.socialcloud.uikit.sample.liveChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatFragment
import com.amity.socialcloud.uikit.sample.R
import com.amity.socialcloud.uikit.sample.databinding.AmityFragmentLiveChatBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AmityLiveChatBottomSheetFragment private constructor(
	private val channelId: String,
	private val subChannelId: String
): BottomSheetDialogFragment() {
	
	lateinit var binding: AmityFragmentLiveChatBottomSheetBinding
	
	override fun getTheme(): Int = R.style.BottomSheetDialogTheme
	
	@OptIn(UnstableApi::class)
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = AmityFragmentLiveChatBottomSheetBinding.inflate(inflater, container, false)
		val messageListFragment = AmityLiveChatFragment.newInstance(
			channelId = channelId,
		).build()
		val transaction = childFragmentManager.beginTransaction()
		transaction.add(R.id.messageListContainer, messageListFragment, messageListFragment.tag)
		transaction.addToBackStack(messageListFragment.tag)
		transaction.commit()
		val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
		bottomSheetBehavior.isDraggable = false
		bottomSheetBehavior.isHideable = false
		bottomSheetBehavior.skipCollapsed = true
		bottomSheetBehavior.isFitToContents = false
		return binding.root
	}
	
	companion object {
		
		class Builder internal constructor(
			private val channelId: String,
			private val subChannelId: String
		) {
			fun build(): AmityLiveChatBottomSheetFragment {
				return AmityLiveChatBottomSheetFragment(channelId = channelId, subChannelId = subChannelId)
			}
		}
		fun newInstance(
			channelId: String,
			subChannelId: String
		): Builder {
			return Builder(channelId = channelId, subChannelId = subChannelId)
		}
	}
}
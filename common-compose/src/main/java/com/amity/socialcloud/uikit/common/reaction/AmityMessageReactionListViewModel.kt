package com.amity.socialcloud.uikit.common.reaction

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityReactionType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityMessageReactionListViewModel : AmityBaseViewModel() {
	
	private val _sheetUIState by lazy {
		MutableStateFlow<AmityMessageReactionListSheetUIState>(AmityMessageReactionListSheetUIState.CloseSheet)
	}
	val sheetUIState get() = _sheetUIState
	
	fun updateSheetUIState(uiState: AmityMessageReactionListSheetUIState) {
		viewModelScope.launch {
			_sheetUIState.value = uiState
		}
	}
	
	sealed class  AmityMessageReactionListSheetUIState {
		
		data class OpenSheet(
			val message: AmityMessage,
			val reactionName: String? = null,
		) : AmityMessageReactionListSheetUIState()
		
		object CloseSheet : AmityMessageReactionListSheetUIState()
	}
}
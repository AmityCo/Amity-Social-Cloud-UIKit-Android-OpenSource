package com.amity.socialcloud.uikit.community.compose.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PollStateUiState(
    val postId: String? = null,
    val isExpanded: Boolean = false,
    val isResultMode: Boolean = false,
    val selectedOption: MutableList<Int> = mutableListOf()
)

class AmityPostPollElementViewModel(postId: String) : AmityBaseViewModel() {
    private val _uiState = MutableStateFlow<MutableList<PollStateUiState>>(mutableListOf())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            mutableListOf(PollStateUiState(postId = postId))
        }
    }

    fun updatePollState(postId: String, isExpanded: Boolean, isResultMode: Boolean, selectedOption: MutableList<Int> = mutableListOf()) {
        _uiState.update { currentState ->
            val updatedState = currentState.toMutableList()
            val newState = PollStateUiState(
                postId = postId,
                isExpanded = isExpanded,
                isResultMode = isResultMode,
                selectedOption = selectedOption
            )
            val index = updatedState.indexOfFirst { it.postId == postId }
            if (index >= 0) {
                updatedState[index] = newState
            } else {
                updatedState.add(newState)
            }
            updatedState
        }
    }

    companion object {
        fun create(postId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AmityPostPollElementViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return AmityPostPollElementViewModel(postId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
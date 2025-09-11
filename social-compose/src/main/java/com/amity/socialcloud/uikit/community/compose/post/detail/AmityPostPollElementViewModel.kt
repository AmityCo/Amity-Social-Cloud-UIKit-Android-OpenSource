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
            mutableListOf(PollStateUiState())
        }
    }

    fun updatePollState(postId: String, isExpanded: Boolean, isResultMode: Boolean, selectedOption: MutableList<Int> = mutableListOf()) {
        _uiState.update { currentState ->
            currentState.apply {
                if (currentState.any { it.postId == postId }) {
                    // Update existing state
                    val index = currentState.indexOfFirst { it.postId == postId }
                    this[index] = PollStateUiState(
                        postId = postId,
                        isExpanded = isExpanded,
                        isResultMode = isResultMode,
                        selectedOption = selectedOption
                    )
                } else {
                    // Add new state
                    this.add(
                        PollStateUiState(
                            postId = postId,
                            isExpanded = isExpanded,
                            isResultMode = isResultMode,
                            selectedOption = selectedOption
                        )
                    )
                }
            }
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
package com.amity.socialcloud.uikit.community.compose.story.hyperlink

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow

class AmityStoryHyperlinkComponentViewModel : AmityBaseViewModel() {

    private val _urlValidation by lazy {
        MutableStateFlow<AmityStoryHyperlinkValidationUIState>(AmityStoryHyperlinkValidationUIState.Initial)
    }
    val urlValidation get() = _urlValidation

    private val _textValidation by lazy {
        MutableStateFlow<AmityStoryHyperlinkValidationUIState>(AmityStoryHyperlinkValidationUIState.Initial)
    }
    val textValidation get() = _textValidation

    fun validateUrls(url: String) {
        AmityCoreClient.validateUrls(listOf(url))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                _urlValidation.value = AmityStoryHyperlinkValidationUIState.Valid(url)
            }
            .doOnError {
                _urlValidation.value = AmityStoryHyperlinkValidationUIState.Invalid(url)
            }
            .subscribe()
    }

    fun validateTexts(text: String) {
        if (text.isEmpty()) {
            _textValidation.value = AmityStoryHyperlinkValidationUIState.Valid(text)
        } else {
            AmityCoreClient.validateTexts(listOf(text))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    _textValidation.value = AmityStoryHyperlinkValidationUIState.Valid(text)
                }
                .doOnError {
                    _textValidation.value = AmityStoryHyperlinkValidationUIState.Invalid(text)
                }
                .subscribe()
        }
    }

    fun resetValidation() {
        _textValidation.value = AmityStoryHyperlinkValidationUIState.Initial
        _urlValidation.value = AmityStoryHyperlinkValidationUIState.Initial
    }
}

sealed class AmityStoryHyperlinkValidationUIState {
    object Initial : AmityStoryHyperlinkValidationUIState()
    data class Valid(val data: String) : AmityStoryHyperlinkValidationUIState()
    data class Invalid(val data: String) : AmityStoryHyperlinkValidationUIState()
}
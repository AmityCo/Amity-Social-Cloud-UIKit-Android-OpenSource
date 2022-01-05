package com.amity.socialcloud.uikit.chat.editMessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.BR
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityActivityEditMessageBinding
import com.amity.socialcloud.uikit.common.base.AmityBaseActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.amity_activity_edit_message.*
import kotlinx.android.synthetic.main.amity_edit_msg_bar.view.*

class AmityEditMessageActivity :
    AmityBaseActivity<AmityActivityEditMessageBinding, AmityEditMessageViewModel>() {

    private val editMessageViewModel: AmityEditMessageViewModel by viewModels()
    private var messageDisposable: Disposable? = null
    private var editMessageDisposable: Disposable? = null

    companion object {
        private const val INTENT_MESSAGE_ID = "messageId"

        fun newIntent(context: Context, messageId: String): Intent {
            val intent = Intent(context, AmityEditMessageActivity::class.java)
            intent.putExtra(INTENT_MESSAGE_ID, messageId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editMessageViewModel.saveColor.set(ContextCompat.getColor(this, R.color.amityColorPrimary))
        setUpToolbar()
        getMessage()
        lMessage.setOnClickListener {
            requestFocus()
        }
    }

    private fun setUpToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(emToolBar as Toolbar)

        emToolBar.icCross.setOnClickListener {
            this.finish()
        }

        emToolBar.tvSave.setOnClickListener {
            editMessageDisposable = editMessageViewModel.saveMessage().subscribe()
            this.finish()
        }
    }

    private fun getMessage() {
        val messageId = intent.getStringExtra(INTENT_MESSAGE_ID)
        if (messageId != null) {
            messageDisposable = editMessageViewModel.getMessage(messageId).subscribe { message ->
                val messageData = message.getData()
                if (messageData is AmityMessage.Data.TEXT) {
                    editMessageViewModel.message.set(messageData.getText())
                    editMessageViewModel.messageLength = messageData.getText().length
                    editMessageViewModel.textData.set(messageData)
                    editMessageViewModel.observeMessageChange()
                    requestFocus()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (messageDisposable != null && !messageDisposable!!.isDisposed) {
            messageDisposable?.dispose()
        }
        if (editMessageDisposable != null && !editMessageDisposable!!.isDisposed) {
            editMessageDisposable?.dispose()
        }
    }

    override fun getLayoutId(): Int = R.layout.amity_activity_edit_message

    override fun getViewModel(): AmityEditMessageViewModel = editMessageViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private fun requestFocus() {
        etMessage.postDelayed({
            etMessage.requestFocusFromTouch()
            etMessage.setSelection(etMessage.text?.length ?: 0)
            val inputManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(etMessage, 0)
        }, 300)

    }

}
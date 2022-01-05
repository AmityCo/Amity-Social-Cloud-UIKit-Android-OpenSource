package com.amity.socialcloud.uikit.common.common.views.text

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R

class AmityExpandableTextView : AppCompatTextView {
    private var readMoreClicked: Boolean = false
    private var readMoreColor = R.color.amityColorHighlight
    private var originalText: CharSequence? = null
    private var trimmedText: CharSequence? = null
    private var trimmed = false
    private var expandOnlyOnReadMoreClick = false

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        ellipsize = TextUtils.TruncateAt.END
        movementMethod = LinkMovementMethod.getInstance()

    }

    fun showCompleteText() {
        readMoreClicked = false
        trimmed = false
        maxLines = Int.MAX_VALUE
        setText()
    }

    fun isReadMoreClicked(): Boolean {
        return readMoreClicked
    }

    fun setReadMoreClicked(readMoreClicked: Boolean) {
        this.readMoreClicked = readMoreClicked
    }

    fun setExpandOnlyOnReadMoreClick(readMoreClickExpand: Boolean) {
        this.expandOnlyOnReadMoreClick = readMoreClickExpand
    }

    private fun setText() {
        if (!text.endsWith(context.getString(R.string.amity_read_more))) {
            originalText = text
            trimmedText = getTrimmedText(text)
        }
        super.setText(getDisplayedText(), BufferType.SPANNABLE)

    }

    private fun getDisplayedText(): CharSequence? {
        return if (trimmed) trimmedText else originalText
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        trimmed = lineCount >= maxLines
        setText()
    }

    private fun getTrimmedText(text: CharSequence?): CharSequence? {
        if (originalText != null && this.lineCount >= maxLines) {
            return try {
                val newLine = System.getProperty("line.separator") ?: "\n"
                val truncatedSpannableString: SpannableString
                val lastCharShown = layout.getLineVisibleEnd(maxLines - 1)

                val readMoreString = context.getString(R.string.amity_read_more)
                val visibleText = text?.subSequence(0, lastCharShown)
                val displayText: CharSequence
                if (visibleText!!.contains(newLine)) {
                    val lastLineIndex = visibleText.lastIndexOf(newLine)
                    var lastLine = visibleText.subSequence(lastLineIndex + 1,visibleText.length - 1)
                    if (lastLine.length > 30) {
                        lastLine = TextUtils.concat(lastLine.subSequence(
                            0,
                            lastLine.length - readMoreString.length
                        ), readMoreString)
                    } else {
                        lastLine = TextUtils.concat(lastLine, readMoreString)
                    }
                    displayText = TextUtils.concat(visibleText.subSequence(0, lastLineIndex + 1), lastLine)
                } else {
                    displayText = TextUtils.concat(text.subSequence(0, lastCharShown - readMoreString.length), readMoreString)
                }

                val startIndex = displayText.indexOf(readMoreString)
                truncatedSpannableString = SpannableString(displayText)
                truncatedSpannableString.setSpan(
                    getReadMoreSpan(),
                    startIndex,
                    startIndex + readMoreString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                truncatedSpannableString
            } catch (ex: Exception) {
                Log.e("EkoExpandableTextView", "exception $text ${ex.localizedMessage}")
                originalText
            }
        } else {
            return originalText
        }
    }

    fun getVisibleLineCount(): Int {
        return if (this.lineCount >= maxLines)
            maxLines
        else
            this.lineCount
    }

    private fun getReadMoreSpan(): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(textView: View) {
                readMoreClicked = true
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(context, readMoreColor)
            }
        }
    }

    fun setReadMoreColor(color: Int) {
        readMoreColor = color
        setText()
    }
}


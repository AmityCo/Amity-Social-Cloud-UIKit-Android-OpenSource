package com.amity.socialcloud.uikit.community.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.amity.socialcloud.uikit.community.R
import kotlinx.android.synthetic.main.amity_dialog_fragment_community_customize.*

class AmityCommunityCustomizeDialogFragment : DialogFragment() {

    private var listener: AmityCommunityCustomizeDialogFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.amity_dialog_fragment_community_customize,
            container,
            false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return context?.let { c ->
            val frameLayout = FrameLayout(c)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return Dialog(c).apply {
                setCanceledOnTouchOutside(false)
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(frameLayout)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        } ?: run {
            return requireDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialListener()
    }

    private fun initialListener() {
        button_community_settings.setOnClickListener {
            listener?.onClickCommunitySettingsButton()
            dismiss()
        }

        textview_skip.setOnClickListener {
            listener?.onClickSkipButton()
            dismiss()
        }
    }

    fun setDialogListener(listener: AmityCommunityCustomizeDialogFragmentListener) {
        this.listener = listener
    }

    interface AmityCommunityCustomizeDialogFragmentListener {
        fun onClickCommunitySettingsButton()
        fun onClickSkipButton()
    }

    companion object {
        val TAG: String = AmityCommunityCustomizeDialogFragment::class.java.simpleName
        fun newInstance(): AmityCommunityCustomizeDialogFragment {
            return AmityCommunityCustomizeDialogFragment()
        }
    }
}
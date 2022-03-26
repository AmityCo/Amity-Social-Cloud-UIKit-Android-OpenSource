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
import com.amity.socialcloud.uikit.community.databinding.AmityDialogFragmentCommunityCustomizeBinding

class AmityCommunityCustomizeDialogFragment : DialogFragment() {

    private var _binding: AmityDialogFragmentCommunityCustomizeBinding? = null
    private val binding get() = _binding!!

    private var listener: AmityCommunityCustomizeDialogFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AmityDialogFragmentCommunityCustomizeBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.buttonCommunitySettings.setOnClickListener {
            listener?.onClickCommunitySettingsButton()
            dismiss()
        }

        binding.textviewSkip.setOnClickListener {
            listener?.onClickSkipButton()
            dismiss()
        }
    }

    fun setDialogListener(listener: AmityCommunityCustomizeDialogFragmentListener) {
        this.listener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
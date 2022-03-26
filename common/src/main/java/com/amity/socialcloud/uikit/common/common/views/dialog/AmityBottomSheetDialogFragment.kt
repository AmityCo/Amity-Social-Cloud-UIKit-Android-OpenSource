package com.amity.socialcloud.uikit.common.common.views.dialog

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import com.amity.socialcloud.uikit.common.databinding.AmityDialogBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.properties.Delegates

const val EXTRA_PARAM_NAV_MENU = "nav_menu"

@Deprecated("Use AmityBottomSheetDialog instead")
class AmityBottomSheetDialogFragment private constructor() : BottomSheetDialogFragment() {
    private var _binding: AmityDialogBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var navListener: OnNavigationItemSelectedListener? = null
    var menu by Delegates.notNull<Int>()
    private var menuItemCallback: (Menu) -> Unit = {}


    companion object {
        fun newInstance(@MenuRes menu: Int): AmityBottomSheetDialogFragment {
            val args = Bundle()

            val fragment = AmityBottomSheetDialogFragment()
            args.putInt(EXTRA_PARAM_NAV_MENU, menu)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menu = requireArguments().getInt(EXTRA_PARAM_NAV_MENU)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AmityDialogBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        this.navListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.apply {
            menu.clear()
            inflateMenu(this@AmityBottomSheetDialogFragment.menu)
            menuItemCallback.invoke(menu)
            setNavigationItemSelectedListener { item ->
                navListener?.onItemSelected(item)
                dismiss()
                true
            }
        }

    }

    fun menuItem(menuItemCallback: (Menu) -> Unit) {
        this.menuItemCallback = menuItemCallback
    }

    interface OnNavigationItemSelectedListener {
        fun onItemSelected(item: MenuItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
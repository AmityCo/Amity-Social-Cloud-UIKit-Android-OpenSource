package com.amity.socialcloud.uikit.common.common.views.dialog

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import com.amity.socialcloud.uikit.common.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.amity_dialog_bottom_sheet.*
import kotlin.properties.Delegates


const val EXTRA_PARAM_NAV_MENU = "nav_menu"

@Deprecated("Use AmityBottomSheetDialog instead")
class AmityBottomSheetDialogFragment private constructor() : BottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.amity_dialog_bottom_sheet, container, false)
    }

    fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        this.navListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.menu.clear()
        navigationView.inflateMenu(menu)
        menuItemCallback.invoke(navigationView.menu)
        navigationView.setNavigationItemSelectedListener { item ->
            navListener?.onItemSelected(item)
            dismiss()
            true
        }

    }

    fun menuItem(menuItemCallback: (Menu) -> Unit) {
        this.menuItemCallback = menuItemCallback
    }

    interface OnNavigationItemSelectedListener {
        fun onItemSelected(item: MenuItem)
    }

}
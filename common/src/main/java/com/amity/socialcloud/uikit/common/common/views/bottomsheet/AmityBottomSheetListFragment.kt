package com.amity.socialcloud.uikit.common.common.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.model.AmityMenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.amity_bottom_sheet.*

@Deprecated("Use AmityBottomSheetDialog instead")
class AmityBottomSheetListFragment private constructor() : BottomSheetDialogFragment() {

    private lateinit var itemList: ArrayList<AmityMenuItem>
    private var mListener: AmityMenuItemClickListener? = null
    private lateinit var mAdapter: AmityBottomSheetAdapter

    companion object {
        private const val ARG_LIST = "ARG_LIST"
        fun newInstance(list: ArrayList<AmityMenuItem>): AmityBottomSheetListFragment =
            AmityBottomSheetListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_LIST, list)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemList = requireArguments().getParcelableArrayList<AmityMenuItem>(ARG_LIST) ?: arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amity_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = AmityBottomSheetAdapter(itemList, mListener)
        rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        rvBottomSheet.adapter = mAdapter
    }

    fun setMenuItemClickListener(listener: AmityMenuItemClickListener) {
        mListener = listener
    }

}
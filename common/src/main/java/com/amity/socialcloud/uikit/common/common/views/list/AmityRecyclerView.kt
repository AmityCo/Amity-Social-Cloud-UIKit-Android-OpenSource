package com.amity.socialcloud.uikit.common.common.views.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class AmityRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )
}
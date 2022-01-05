package com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet

data class BottomSheetMenuItem(
    val iconResId: Int? = null,
    val colorResId: Int? = null,
    val titleResId: Int,
    val action: () -> Unit
)
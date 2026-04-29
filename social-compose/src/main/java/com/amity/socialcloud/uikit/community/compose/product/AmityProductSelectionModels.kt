package com.amity.socialcloud.uikit.community.compose.product

import androidx.compose.runtime.Immutable

@Immutable
data class AmityProductUi(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
)


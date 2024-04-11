package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape

val AmityUIKitShapes = AmityShapes(
    component = RoundedCornerShape(ZeroCornerSize),
    surface = RoundedCornerShape(ZeroCornerSize)
)

@Immutable
data class AmityShapes(
    val component: Shape,
    val surface: Shape
)
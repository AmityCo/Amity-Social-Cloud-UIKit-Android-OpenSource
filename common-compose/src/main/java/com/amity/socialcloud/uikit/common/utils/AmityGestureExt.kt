package com.amity.socialcloud.uikit.common.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import kotlinx.coroutines.coroutineScope

suspend fun PointerInputScope.interceptTap(
    onTap: ((Offset) -> Unit),
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown(pass = PointerEventPass.Initial)
        val downTime = System.currentTimeMillis()
        val tapTimeout = viewConfiguration.longPressTimeoutMillis
        val tapPosition = down.position

        do {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val currentTime = System.currentTimeMillis()

            val change = event.changes[0]
            //  distance between finger down and finger up position
            val distance = (change.position - tapPosition).getDistance()

            if (change.id == down.id && !change.pressed
                && distance < viewConfiguration.touchSlop  //  distance within slop
            ) {
                change.consume()

                if (currentTime - downTime < tapTimeout) {
                    //  finger up before timeout, assume as tap
                    onTap(change.position)
                }
            }
        } while (event.changes.any { it.id == down.id && it.pressed })
    }
}
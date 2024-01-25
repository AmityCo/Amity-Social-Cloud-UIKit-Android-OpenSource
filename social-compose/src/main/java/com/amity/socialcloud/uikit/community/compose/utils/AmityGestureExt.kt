package com.amity.socialcloud.uikit.community.compose.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

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

suspend fun PointerInputScope.interceptHold(
    onHold: ((Boolean) -> Unit)
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown(pass = PointerEventPass.Initial)

        //  finger down, assume as hold
        onHold(true)

        do {
            val event = awaitPointerEvent(PointerEventPass.Initial)

            val change = event.changes[0]

            if (change.id == down.id && !change.pressed) {
                onHold(false)
            }
        } while (event.changes.any { it.id == down.id && it.pressed })
    }
}

suspend fun PointerInputScope.interceptSwipeDown(
    onSwipeDown: (() -> Unit)
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown(pass = PointerEventPass.Initial)
        val tapPosition = down.position

        do {
            val event = awaitPointerEvent(PointerEventPass.Initial)

            val change = event.changes[0]
            //  distance between finger down and finger up position
            val distance = (change.position - tapPosition).getDistance()

            if (change.id == down.id && !change.pressed
                && distance > viewConfiguration.touchSlop  //  distance exceed slop
            ) {
                change.consume()

                val xChanged = abs(change.position.x - tapPosition.x)
                val yChanged = abs(change.position.y - tapPosition.y)

                if (xChanged < 100 && yChanged > 200) {
                    onSwipeDown()
                }
            }
        } while (event.changes.any { it.id == down.id && it.pressed })
    }
}

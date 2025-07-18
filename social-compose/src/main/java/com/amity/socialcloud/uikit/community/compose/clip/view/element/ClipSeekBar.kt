package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ClipSeekBar(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    onDragStateChanged: (Boolean) -> Unit = {}
) {
    var duration by remember { mutableLongStateOf(0L) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(isDragging) {
        onDragStateChanged(isDragging)
//        if (isDragging) {
//            exoPlayer.pause()
//        }
    }

    // Update progress periodically
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (!isDragging) {
                duration = exoPlayer.duration.takeIf { it > 0 } ?: 0L
                currentPosition = exoPlayer.currentPosition
            }
            delay(100) // Update every 100ms
        }
    }

    val progress = if (duration > 0) {
        (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column {
        ClipDurationDisplay(
            currentPosition = currentPosition,
            duration = duration,
            isDragging = isDragging,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            exoPlayer.play() // Resume playback after drag
                        },
                        onDrag = { change, _ ->
                            if (duration > 0) {
                                // Calculate seek position based on current drag position
                                val seekRatio = (change.position.x / size.width).coerceIn(0f, 1f)
                                val seekPosition = (seekRatio * duration).toLong()
                                exoPlayer.seekTo(seekPosition)
                                currentPosition = seekPosition // Update current position immediately
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (duration > 0) {
                            val seekPosition = (offset.x / size.width * duration).toLong()
                            exoPlayer.seekTo(seekPosition)
                        }
                    }
                }
        ) {
            Box(
                modifier = modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 12.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ClipDurationDisplay(
    currentPosition: Long,
    duration: Long,
    isDragging: Boolean,
    modifier: Modifier = Modifier
) {
    if (isDragging && duration > 0) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDuration(currentPosition),
                color = Color.White,
                style = AmityTheme.typography.body
            )
            Text(
                text = formatDuration(duration),
                color = Color.White,
                style = AmityTheme.typography.body
            )
        }
    }
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60)) % 24
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}
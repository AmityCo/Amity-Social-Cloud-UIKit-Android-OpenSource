package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.delay

@Composable
fun AmityCircularProgressWithCountDownTimer(totalTime: Int, onTimeUp: () -> Unit) {
    var timeLeft by remember { mutableIntStateOf(totalTime) }
    val progress by remember { derivedStateOf { timeLeft.toFloat() / totalTime } }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // 1 second delay
            timeLeft--
        } else {
            onTimeUp() // Trigger the event when time is up
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.amity_v4_create_livestream_count_down_title),
                color = Color.White,
                style = AmityTheme.typography.titleLegacy.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(72.dp)
                        .height(72.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                    trackColor = Color.White.copy(0.2f),
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = timeLeft.toString(),
                    color = Color.White,
                    style = AmityTheme.typography.display
                )
            }
        }
    }


}
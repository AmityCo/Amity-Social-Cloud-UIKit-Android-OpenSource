package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@ExperimentalMaterial3Api
@Composable
fun AmityTimePickerDialog(
    timePickerState: TimePickerState,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
) {
    val backgroundColor = AmityTheme.colors.background
    val baseColor = AmityTheme.colors.base
    val baseShade4Color = AmityTheme.colors.baseShade4
    val primaryColor = AmityTheme.colors.primary
    val primaryShade3Color = AmityTheme.colors.primaryShade3

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = backgroundColor,
        ),
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                text = "Select time",
                color = baseColor
            )
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = baseShade4Color,
                    clockDialSelectedContentColor = Color.White,
                    selectorColor = primaryColor,
                    periodSelectorSelectedContainerColor = primaryShade3Color,
                    periodSelectorSelectedContentColor = primaryColor,
                    timeSelectorSelectedContainerColor = primaryShade3Color,
                    timeSelectorUnselectedContainerColor = baseShade4Color,
                    timeSelectorSelectedContentColor = primaryColor,
                )
            )
        }
    }
}
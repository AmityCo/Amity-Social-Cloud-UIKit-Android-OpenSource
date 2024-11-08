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
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = AmityTheme.colors.background,
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
                color = AmityTheme.colors.base
            )
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = AmityTheme.colors.baseShade4,
                    clockDialSelectedContentColor = Color.White,
                    selectorColor = AmityTheme.colors.primary,
                    periodSelectorSelectedContainerColor = AmityTheme.colors.primaryShade3,
                    periodSelectorSelectedContentColor = AmityTheme.colors.highlight,
                    timeSelectorSelectedContainerColor = AmityTheme.colors.primaryShade3,
                    timeSelectorUnselectedContainerColor = AmityTheme.colors.baseShade4,
                    timeSelectorSelectedContentColor = AmityTheme.colors.primary,
                )
            )
        }
    }
}
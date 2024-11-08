package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@ExperimentalMaterial3Api
@Composable
fun AmityDatePickerDialog(
    datePickerState: DatePickerState,
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
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = AmityTheme.colors.primary,
                selectedYearContainerColor = AmityTheme.colors.primary,
                todayContentColor = AmityTheme.colors.primary,
                todayDateBorderColor = AmityTheme.colors.primary
            )
        )
    }
}
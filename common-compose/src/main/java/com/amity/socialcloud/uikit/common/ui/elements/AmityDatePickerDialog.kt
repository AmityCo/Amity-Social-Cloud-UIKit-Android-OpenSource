package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@ExperimentalMaterial3Api
@Composable
fun AmityDatePickerDialog(
    datePickerState: DatePickerState,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
) {
    val backgroundColor = AmityTheme.colors.background
    val primaryColor = AmityTheme.colors.primary

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = backgroundColor,
        ),
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = primaryColor,
                selectedYearContainerColor = primaryColor,
                todayContentColor = primaryColor,
                todayDateBorderColor = primaryColor,
                containerColor = backgroundColor,
                headlineContentColor = AmityTheme.colors.base,
                navigationContentColor = AmityTheme.colors.base,
                dayContentColor = AmityTheme.colors.base,
                subheadContentColor = AmityTheme.colors.base,
                disabledDayContentColor = AmityTheme.colors.baseShade3,
                titleContentColor = AmityTheme.colors.base,
                dividerColor = amityColorWhite.copy(alpha = 0.17f),
                currentYearContentColor = primaryColor,
                dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                    cursorColor = primaryColor,
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedLabelColor = primaryColor,
                    focusedTextColor = AmityTheme.colors.base,
                    focusedBorderColor = primaryColor
                )
            )
        )
    }
}
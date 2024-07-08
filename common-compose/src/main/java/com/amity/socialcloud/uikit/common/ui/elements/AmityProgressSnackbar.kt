package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmityProgressSnackbar(
    modifier: Modifier = Modifier,
    data: AmityProgressSnackbarVisuals,
) {
    Snackbar(
        containerColor = AmityTheme.colors.secondary,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {

            CircularProgressIndicator(
                color = AmityTheme.colors.highlight,
                trackColor = Color.White,
                strokeWidth = 2.dp,
                modifier = modifier.size(24.dp),
            )

            Text(
                text = data.message,
                color = Color.White,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
            )
        }
    }
}


data class AmityProgressSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
    override val withDismissAction: Boolean = false,
) : SnackbarVisuals

@Preview(showBackground = true)
@Composable
fun AmityProgressSnackbarPreview() {
    AmityProgressSnackbar(
        data = AmityProgressSnackbarVisuals(
            message = "This is a snackbar."
        )
    )
}
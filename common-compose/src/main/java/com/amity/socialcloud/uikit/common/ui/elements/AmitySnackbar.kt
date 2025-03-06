package com.amity.socialcloud.uikit.common.ui.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmitySnackbar(
    modifier: Modifier = Modifier,
    data: AmitySnackbarVisuals,
) {
    Snackbar(
        containerColor = AmityTheme.colors.secondary,
        modifier = modifier
            .fillMaxWidth()
            .zIndex(100f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(id = data.drawableRes),
                contentDescription = null,
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

data class AmitySnackbarVisuals(
    override val message: String,
    @DrawableRes var drawableRes: Int = R.drawable.amity_ic_warning,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    override val withDismissAction: Boolean = false,
    var additionalHeight: Int = 0,
) : SnackbarVisuals

@Preview(showBackground = true)
@Composable
fun AmitySnackbarPreview() {
    AmitySnackbar(
        data = AmitySnackbarVisuals(
            message = "This is a snackbar."
//        text = "This is a snackbar. sample text laje rle re lr;e rl;e r; er;e ;ewjlkqjfojioajr eoi hroerjro gerhopjw owebnpowj w4"
        )
    )
}
package com.amity.socialcloud.uikit.community.compose.ui.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmitySnackbar(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int = R.drawable.amity_ic_warning,
    text: String,
    containerColor: Color = Color(0xFF292B32),
) {
    Snackbar(
        containerColor = containerColor,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                modifier = modifier.size(24.dp),
            )
            Text(
                text = text,
                color = Color.White,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmitySnackbarPreview() {
    AmitySnackbar(
        text = "This is a snackbar."
//        text = "This is a snackbar. sample text laje rle re lr;e rl;e r; er;e ;ewjlkqjfojioajr eoi hroerjro gerhopjw owebnpowj w4"
    )
}
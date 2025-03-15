package com.clp3z.mysugrtest.features.bottomsheet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.COLLAPSED
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.EXPANDED
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.INITIAL
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Size
import com.clp3z.mysugrtest.framework.theme.Spacing

enum class SheetHandleState {
    INITIAL,
    COLLAPSED,
    EXPANDED
}

@Composable
fun SheetTitleHandle(
    sheetHandleState: SheetHandleState,
    onHandleClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(Size.sheet_peek_height)
            .padding(Spacing.spacing_8)
    ) {
        IconButton(onClick = { onHandleClick() }) {
            Image(
                imageVector = when (sheetHandleState) {
                    INITIAL, EXPANDED -> Icons.Default.KeyboardArrowDown
                    COLLAPSED -> Icons.Default.KeyboardArrowUp
                },
                contentDescription = null,
                modifier = Modifier.size(Size.icon_size)
            )
        }
        Text(
            text = stringResource(R.string.add_your_glucose_measurement),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun SheetTitleHandlePreview() {
    BoxPreview {
        SheetTitleHandle(
            sheetHandleState = INITIAL,
            onHandleClick = {}
        )
    }
}

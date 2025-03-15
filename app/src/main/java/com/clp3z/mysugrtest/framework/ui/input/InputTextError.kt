package com.clp3z.mysugrtest.framework.ui.input

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.framework.theme.BoxPreview

@Composable
fun InputTextError(
    message: String?,
    isValid: Boolean
) {
    if (!isValid && message != null) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium.copy(Color.Red)
        )
    }
}

@Preview
@Composable
private fun InputTextErrorPreview() {
    BoxPreview {
        InputTextError(
            message = "Value should not be negative",
            isValid = false
        )
    }
}

package com.clp3z.mysugrtest.features.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Spacing
import com.clp3z.mysugrtest.framework.ui.input.InputFieldState
import com.clp3z.mysugrtest.framework.ui.input.InputTextField
import com.clp3z.mysugrtest.framework.ui.input.rememberInputFieldState

@Composable
fun GlucoseInputField(
    selectedUnit: String,
    inputFieldState: InputFieldState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        InputTextField(
            inputFieldState = inputFieldState,
            modifier = Modifier.weight(3f),
        )
        Text(
            text = selectedUnit,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }
}

@Preview
@Composable
private fun GlucoseInputFieldPreview() {
    BoxPreview {
        GlucoseInputField(
            selectedUnit = "mg/dl",
            inputFieldState = rememberInputFieldState(),
            modifier = Modifier.padding(horizontal = Spacing.spacing_16)
        )
    }
}

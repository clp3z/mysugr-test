package com.clp3z.mysugrtest.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Spacing
import com.clp3z.mysugrtest.framework.ui.input.InputFieldState
import com.clp3z.mysugrtest.framework.ui.input.rememberInputFieldState

@Composable
fun BottomSheetLayout(
    average: Float?,
    selectedUnit: String,
    inputFieldState: InputFieldState,
    onUnitSelected: (String) -> Unit,
    onSaveMeasurementClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.spacing_16)
    ) {
        average?.let {
            GlucoseMeasurement(
                average = it,
                selectedUnit = selectedUnit
            )
        }
        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.5f),
            modifier = Modifier.padding(vertical = Spacing.spacing_16)
        )
        Text(
            text = stringResource(R.string.add_glucose_measurement).uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(color = Color.DarkGray),
            fontWeight = FontWeight.SemiBold
        )
        UnitRadioGroup(
            onUnitSelected = onUnitSelected,
            modifier = Modifier.padding(top = Spacing.spacing_8)
        )
        GlucoseInputField(
            selectedUnit = selectedUnit,
            inputFieldState = inputFieldState,
            modifier = Modifier.padding(top = Spacing.spacing_8, bottom = Spacing.spacing_16)
        )
        Button(onClick = { onSaveMeasurementClick() }) {
            Text(text = stringResource(R.string.save_measurement))
        }
    }
}

@Preview
@Composable
private fun BottomSheetLayoutPreview() {
    val inputFieldState = rememberInputFieldState()
    BoxPreview {
        BottomSheetLayout(
            average = 100f,
            selectedUnit = "mg/dL",
            inputFieldState = inputFieldState,
            onUnitSelected = {},
            onSaveMeasurementClick = {}
        )
    }
}

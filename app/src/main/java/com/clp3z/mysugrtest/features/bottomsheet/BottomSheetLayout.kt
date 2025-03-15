package com.clp3z.mysugrtest.features.bottomsheet

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.bottomsheet.components.GlucoseMeasurement
import com.clp3z.mysugrtest.features.bottomsheet.components.UnitRadioGroup
import com.clp3z.mysugrtest.features.toString
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Spacing
import com.clp3z.mysugrtest.framework.ui.input.InputFieldState
import com.clp3z.mysugrtest.framework.ui.input.InputTextField
import com.clp3z.mysugrtest.framework.ui.input.rememberInputFieldState

@Composable
fun BottomSheetLayout(
    average: Float?,
    selectedUnit: GlucoseUnit,
    inputFieldState: InputFieldState,
    onUnitSelected: (GlucoseUnit) -> Unit,
    onSaveMeasurementClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.spacing_16)
    ) {
        average?.let {
            GlucoseMeasurement(
                average = it,
                selectedUnit = selectedUnit.toString(context)
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
        InputTextField(
            label = selectedUnit.toString(context),
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
    inputFieldState.onFieldValidation { Pair(false, "Invalid value") }
    BoxPreview {
        BottomSheetLayout(
            average = 100f,
            selectedUnit = GlucoseUnit.MG_DL,
            inputFieldState = inputFieldState,
            onUnitSelected = {},
            onSaveMeasurementClick = {}
        )
    }
}

package com.clp3z.mysugrtest.features.bottomsheet.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.common.BottomSheetViewModel
import com.clp3z.mysugrtest.framework.ui.input.rememberInputFieldState

@Composable
fun BottomSheet(
    viewModel: BottomSheetViewModel = hiltViewModel(),
    selectedUnit:  GlucoseUnit,
    onUnitSelected: (GlucoseUnit) -> Unit
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val inputFieldState = rememberInputFieldState()

    LaunchedEffect(Unit) {
        viewModel.initialize(selectedUnit)
        viewModel.collectMeasurementInputField(inputFieldState.data)
    }

    LaunchedEffect(viewState.measurement) {
        inputFieldState.onValueChange(viewState.measurement)
    }

    LaunchedEffect(viewState.isMeasurementValid) {
        inputFieldState.onFieldValidation {
            when (viewState.isMeasurementValid) {
                true -> true to ""
                false -> false to context.getString(R.string.glucose_measurement_value_is_invalid)
            }
        }
    }

    BottomSheetLayout(
        average = viewState.average,
        selectedUnit = viewState.selectedUnit,
        inputFieldState = inputFieldState,
        onUnitSelected = {
            viewModel.onUnitSelected(it)
            onUnitSelected(it)
        },
        onSaveMeasurementClick = { viewModel.onSaveMeasurementClick() }
    )
}

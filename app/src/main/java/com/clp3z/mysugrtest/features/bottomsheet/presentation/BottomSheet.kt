package com.clp3z.mysugrtest.features.bottomsheet.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.bottomsheet.util.isMeasurementValid
import com.clp3z.mysugrtest.framework.ui.input.rememberInputFieldState

@Composable
fun BottomSheet(
    viewModel: BottomSheetViewModel = hiltViewModel(),
    onUnitSelected: (GlucoseUnit) -> Unit
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val inputFieldState = rememberInputFieldState()

    Log.d("", "*** Is recomposing...")

    LaunchedEffect(Unit) {
        viewModel.initialize()
        viewModel.collectMeasurementInputField(inputFieldState.data)
    }

    LaunchedEffect(viewState.measurement) {
        inputFieldState.onValueChange(viewState.measurement)
        inputFieldState.onFieldValidation {
            when (viewState.measurement.isMeasurementValid()) {
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

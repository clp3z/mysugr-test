package com.clp3z.mysugrtest.features.measurements.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.common.BottomSheetViewModel
import com.clp3z.mysugrtest.features.measurements.components.MeasurementList

@Composable
fun Measurements(
    viewModel: BottomSheetViewModel = hiltViewModel(),
    selectedUnit: GlucoseUnit,
    modifier: Modifier
) {

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(selectedUnit)
    }

    LaunchedEffect(selectedUnit) {
        viewModel.onUnitSelected(selectedUnit)
    }

    MeasurementList(
        measurements = viewState.measurements,
        modifier = modifier
    )
}

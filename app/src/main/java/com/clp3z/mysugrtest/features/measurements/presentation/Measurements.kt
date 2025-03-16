package com.clp3z.mysugrtest.features.measurements.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.clp3z.mysugrtest.features.measurements.components.MeasurementList

@Composable
fun Measurements(
    viewModel: MeasurementsViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    MeasurementList(
        measurements = viewState.measurements,
        modifier = modifier
    )
}

@Preview
@Composable
private fun MeasurementsPreview() {
    // Measurements(previewMeasurements)
}

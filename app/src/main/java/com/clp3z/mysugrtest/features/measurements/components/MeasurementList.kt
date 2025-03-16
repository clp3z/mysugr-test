package com.clp3z.mysugrtest.features.measurements.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.features.measurements.previewMeasurements
import com.clp3z.mysugrtest.framework.theme.FullScreenPreview
import com.clp3z.mysugrtest.framework.theme.Size
import com.clp3z.mysugrtest.framework.theme.Spacing

@Composable
fun MeasurementList(
    modifier: Modifier = Modifier,
    measurements: List<GlucoseMeasurement>
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(
            items = measurements,
            key = { _, measurement -> measurement.id }
        ) { index, measurement ->
            MeasurementRow(
                modifier = Modifier.padding(horizontal = Spacing.spacing_16),
                glucoseMeasurement = measurement
            )
            if (index != measurements.lastIndex) {
                HorizontalDivider(
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = Spacing.spacing_16)
                )
            } else {
                Spacer(modifier = Modifier.padding(bottom = Size.sheet_peek_height))
            }
        }
    }
}

@Preview
@Composable
private fun PlanetsListPreview() {
    FullScreenPreview {
        MeasurementList(measurements = previewMeasurements)
    }
}

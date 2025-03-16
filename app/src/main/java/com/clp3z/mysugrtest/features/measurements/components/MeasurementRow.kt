package com.clp3z.mysugrtest.features.measurements.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.features.common.toPresentationValue
import com.clp3z.mysugrtest.features.common.toString
import com.clp3z.mysugrtest.features.measurements.previewMeasurement
import com.clp3z.mysugrtest.framework.theme.Spacing

@Composable
fun MeasurementRow(
    glucoseMeasurement: GlucoseMeasurement,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val glucoseUnit = glucoseMeasurement.unit
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.spacing_16)
    ) {
        Text(
            text = glucoseMeasurement.value.toPresentationValue(glucoseUnit),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = glucoseUnit.toString(context),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(3f)
        )
    }
}

@Preview
@Composable
private fun MeasurementRowPreview() {
    MeasurementRow(glucoseMeasurement = previewMeasurement)
}
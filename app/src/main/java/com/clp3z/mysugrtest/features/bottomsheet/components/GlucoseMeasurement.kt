package com.clp3z.mysugrtest.features.bottomsheet.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.framework.theme.Spacing

@Composable
fun GlucoseMeasurement(
    average: String,
    selectedUnit: String
) {
    Surface (
        shape = RoundedCornerShape(Spacing.spacing_4),
        color = Color.LightGray.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.your_average_is, average, selectedUnit),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(Spacing.spacing_16)
        )
    }
}

@Preview
@Composable
private fun GlucoseMeasurementPreview() {
    GlucoseMeasurement(
        average = "100",
        selectedUnit = "mg/dl"
    )
}

package com.clp3z.mysugrtest.features.bottomsheet.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.R
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.toGlucoseUnit
import com.clp3z.mysugrtest.features.toString
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Spacing

@Composable
fun UnitRadioGroup(
    selectedUnit: GlucoseUnit,
    onUnitSelected: (GlucoseUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val units = listOf(stringResource(R.string.mg_dl), stringResource(R.string.mmol_l))
    var selected by remember { mutableIntStateOf(units.indexOf(selectedUnit.toString(context))) }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        units.forEachIndexed { index, unit ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = Spacing.spacing_16)
            ) {
                RadioButton(
                    selected = selected == index,
                    onClick = {
                        val unitToSelect = units[index].toGlucoseUnit(context)
                        selected = index
                        onUnitSelected(unitToSelect)
                        Log.e("UnitRadioGroup", "*** selected unit: $unitToSelect")
                    }
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview
@Composable
private fun UnitRadioGroupPreview() {
    BoxPreview {
        UnitRadioGroup(
            selectedUnit = GlucoseUnit.MMOL_L,
            onUnitSelected = {}
        )
    }
}

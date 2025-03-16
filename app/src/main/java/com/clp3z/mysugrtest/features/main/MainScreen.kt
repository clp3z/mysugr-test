package com.clp3z.mysugrtest.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetIndicator
import com.clp3z.mysugrtest.features.bottomsheet.presentation.BottomSheet
import com.clp3z.mysugrtest.features.measurements.presentation.Measurements
import com.clp3z.mysugrtest.framework.theme.PailYellow
import com.clp3z.mysugrtest.framework.theme.Size
import com.clp3z.mysugrtest.framework.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    BottomSheetScaffold(
        sheetPeekHeight = Size.sheet_peek_height,
        scaffoldState = scaffoldState,
        containerColor = Color(PailYellow.value),
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topEnd = Spacing.spacing_8, topStart = Spacing.spacing_8),
        sheetShadowElevation = Spacing.spacing_16,
        sheetContent = { BottomSheet() },
        sheetDragHandle = { SheetIndicator(modifier = Modifier.padding(top = Spacing.spacing_16)) }
    ) {
        Measurements(modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
private fun HomeScaffoldPreview() {
    MainScreen()
}

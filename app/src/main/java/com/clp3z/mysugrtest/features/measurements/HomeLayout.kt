package com.clp3z.mysugrtest.features.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.clp3z.mysugrtest.features.bottomsheet.BottomSheet
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.COLLAPSED
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.EXPANDED
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetHandleState.INITIAL
import com.clp3z.mysugrtest.features.bottomsheet.components.SheetTitleHandle
import com.clp3z.mysugrtest.framework.theme.Size
import com.clp3z.mysugrtest.framework.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout() {
    var sheetHandleState: SheetHandleState by remember { mutableStateOf(INITIAL) }
    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetPeekHeight = Size.sheet_peek_height,
        scaffoldState = scaffoldState,
        containerColor = Color.LightGray,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topEnd = Spacing.spacing_8, topStart = Spacing.spacing_8),
        sheetShadowElevation = Spacing.spacing_16,
        sheetContent = { BottomSheet() },
        sheetDragHandle = {
            SheetTitleHandle(
                sheetHandleState = sheetHandleState,
                onHandleClick = {
                    coroutineScope.launch {
                        when (sheetHandleState) {
                            INITIAL, EXPANDED -> {
                                sheetHandleState = COLLAPSED
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            COLLAPSED -> {
                                sheetHandleState = EXPANDED
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    }
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.LightGray)
        )
    }
}

@Preview
@Composable
private fun HomeScaffoldPreview() {
    HomeLayout()
}

package com.clp3z.mysugrtest.features.bottomsheet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clp3z.mysugrtest.framework.theme.Spacing

@Composable
fun SheetIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(60.dp)
                .height(Spacing.spacing_4)
                .background(color = color)
        )
    }
}

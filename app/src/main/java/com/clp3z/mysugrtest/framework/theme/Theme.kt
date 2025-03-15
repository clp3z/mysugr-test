package com.clp3z.mysugrtest.framework.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color.White,
    onSurface = Color.DarkGray
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color.White,
    onSurface = Color.DarkGray
)

val radioButtonColors = RadioButtonColors(
    selectedColor = Color.Black,
    unselectedColor = Color.Gray,
    disabledUnselectedColor = Color.LightGray,
    disabledSelectedColor = Color.LightGray
)

val buttonColors = ButtonColors(
    containerColor = Color.Black,
    contentColor = Color.White,
    disabledContainerColor = Color.LightGray,
    disabledContentColor = Color.Gray
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun FullScreenPreview(content: @Composable () -> Unit) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AppTheme(content = content)
        }
    }
}

@Composable
fun BoxPreview(
    content: @Composable () -> Unit
) {
    AppTheme{
        Surface {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

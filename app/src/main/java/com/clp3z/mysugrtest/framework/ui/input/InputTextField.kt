package com.clp3z.mysugrtest.framework.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clp3z.mysugrtest.framework.theme.BoxPreview
import com.clp3z.mysugrtest.framework.theme.Spacing

private fun getDefaultKeyboardOptions() = KeyboardOptions(
    keyboardType = KeyboardType.Number,
    imeAction = ImeAction.Done
)

@Composable
fun InputTextField(
    inputFieldState: InputFieldState,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = getDefaultKeyboardOptions(),
) {
    val inputData by inputFieldState.data.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.spacing_8),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(Spacing.spacing_8),
            color = Color.White,
            border = when (inputData.isValid) {
                true -> BorderStroke(
                    width = 1.5.dp,
                    color = Color.DarkGray
                )
                false -> BorderStroke(
                    width = 1.5.dp,
                    color = Color.Red
                )
            },
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            BasicTextField(
                value = inputData.text,
                onValueChange = { inputFieldState.onValueChange(it) },
                keyboardOptions = keyboardOptions,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.spacing_16)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
        }
        InputTextError(
            message = inputData.error,
            isValid = inputData.isValid
        )
    }
}

@Preview
@Composable
private fun InputTextFieldPreview() {
    val inputFieldState = rememberInputFieldState()
    inputFieldState.onValueChange("13")
    inputFieldState.onFieldValidation { Pair(false, "Some error") }

    BoxPreview {
        InputTextField(
            inputFieldState = inputFieldState,
            modifier = Modifier.padding(horizontal = Spacing.spacing_16)
        )
    }
}

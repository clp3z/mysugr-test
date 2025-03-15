package com.clp3z.mysugrtest.framework.ui.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun rememberInputFieldState() = remember { InputFieldState() }

data class InputFieldData(
    val text: String = "",
    val isValid: Boolean = true,
    val error: String? = null
)

class InputFieldState {
    private val _data = MutableStateFlow(InputFieldData())
    val data: StateFlow<InputFieldData> = _data.asStateFlow()

    val text get() = data.value.text.trim()

    fun onValueChange(value: String) {
        _data.update { it.copy(text = value, isValid = true, error = null) }
    }

    fun onFieldValidation(isFieldValid: (String) -> Pair<Boolean, String>): Boolean {
        val (isValid, error) = isFieldValid(text)
        _data.update { it.copy(isValid = isValid, error = error) }
        return isValid
    }
}

package com.clp3z.mysugrtest.features.bottomsheet.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.domain.AddGlucoseMeasurementUseCase
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.bottomsheet.util.isMeasurementValid
import com.clp3z.mysugrtest.features.common.toPresentationValue
import com.clp3z.mysugrtest.features.common.toUnitValue
import com.clp3z.mysugrtest.framework.ui.input.InputFieldData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val getGlucoseMeasurementsUseCase: GetGlucoseMeasurementsUseCase,
    private val addGlucoseMeasurementUseCase: AddGlucoseMeasurementUseCase
) : ViewModel() {

    data class ViewState(
        val average: Float? = null,
        val selectedUnit: GlucoseUnit = GlucoseUnit.MG_DL,
        val measurement: String = "",
        val isMeasurementValid: Boolean = true
    )

    private val selectedUnit get () = _viewState.value.selectedUnit
    private val measurement get () = _viewState.value.measurement
    private val average get () = _viewState.value.average

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun initialize(selectedUnit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = selectedUnit) }
        getGlucoseMeasurementsUseCase().collect { result ->
            result.fold(
                ifLeft = {},
                ifRight = { measurements ->
                    _viewState.update {
                        it.copy(
                            average = measurements
                                .takeIf { list -> list.isNotEmpty() }
                                ?.map { measurement -> measurement.value }
                                ?.average()
                                ?.toFloat()
                        )
                    }
                }
            )
        }
    }

    fun collectMeasurementInputField(inputFieldDataFlow: StateFlow<InputFieldData>) {
        viewModelScope.launch {
            inputFieldDataFlow.collect { data ->
                _viewState.update { it.copy(measurement = data.text) }
            }
        }
    }

    fun onUnitSelected(unit: GlucoseUnit) = viewModelScope.launch {
        Log.d("BottomSheetViewModel", "*** measurement value: $measurement, selectedUnit: $selectedUnit")
        if (unit != selectedUnit) {
            _viewState.update { it.copy(selectedUnit = unit) }
            if (measurement.isNotBlank() && measurement.isMeasurementValid()) {
                _viewState.update {
                    it.copy(
                        measurement = measurement
                            .toFloat()
                            .toUnitValue(unit)
                            .toPresentationValue(unit)
                    )
                }
            }
            if (average != null) {
                _viewState.update { it.copy(average = average?.toUnitValue(unit)) }
            }
        }
    }

    fun onSaveMeasurementClick() = viewModelScope.launch {
        if (measurement.isNotBlank() && measurement.isMeasurementValid()) {
            addGlucoseMeasurementUseCase(
                measurement = GlucoseMeasurement(
                    value = measurement.toFloat(),
                    unit = selectedUnit
                )
            )
            _viewState.update { it.copy(measurement = "") }
        }
    }
}

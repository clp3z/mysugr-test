package com.clp3z.mysugrtest.features.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.data.toUnknownError
import com.clp3z.mysugrtest.domain.AddGlucoseMeasurementUseCase
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.Error
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.bottomsheet.util.isMeasurementValid
import com.clp3z.mysugrtest.framework.ui.input.InputFieldData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val getGlucoseMeasurementsUseCase: GetGlucoseMeasurementsUseCase,
    private val addGlucoseMeasurementUseCase: AddGlucoseMeasurementUseCase
) : ViewModel() {

    data class ViewState(
        val measurements: List<GlucoseMeasurement> = emptyList(),
        val average: Float? = null,
        val selectedUnit: GlucoseUnit = GlucoseUnit.MG_DL,
        val measurement: String = "",
        val isMeasurementValid: Boolean = true
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<Error>()
    val errorEvent = _errorEvent.asSharedFlow()

    private var originalMeasurements: List<GlucoseMeasurement> = emptyList()
    private val selectedUnit get () = _viewState.value.selectedUnit
    private val measurement get () = _viewState.value.measurement
    private val average get () = _viewState.value.average
    private val isMeasurementValid get () = _viewState.value.isMeasurementValid

    fun initialize(selectedUnit: GlucoseUnit) {
        _viewState.update { it.copy(selectedUnit = selectedUnit) }
        calculateAverage()
    }

    private fun calculateAverage() = viewModelScope.launch {
        getGlucoseMeasurementsUseCase()
            .catch { throwable -> _errorEvent.emit(throwable.toUnknownError()) }
            .collect { result ->
                result.fold(
                    ifLeft = { _errorEvent.emit(it) }  ,
                    ifRight = { measurements ->
                        originalMeasurements = measurements
                        updateAverage(selectedUnit = selectedUnit)
                    }
                )
            }
    }

    private fun updateAverage(selectedUnit: GlucoseUnit) = viewModelScope.launch {
        val convertedMeasurements = convertMeasurements(originalMeasurements, selectedUnit)
        _viewState.update {
            it.copy(
                measurements = convertedMeasurements,
                average = convertedMeasurements
                    .takeIf { list -> list.isNotEmpty() }
                    ?.map { measurement -> measurement.value }
                    ?.average()
                    ?.toFloat()
            )
        }
    }

    fun collectMeasurementInputField(inputFieldDataFlow: StateFlow<InputFieldData>) {
        viewModelScope.launch {
            inputFieldDataFlow.collect { data ->
                _viewState.update {
                    it.copy(
                        measurement = data.text,
                        isMeasurementValid = data.text.isMeasurementValid()
                    )
                }
            }
        }
    }

    fun onUnitSelected(unit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = unit) }
        if (measurement.isNotBlank() && isMeasurementValid) {
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
            updateAverage(selectedUnit = unit)
        }
    }

    fun onSaveMeasurementClick() = viewModelScope.launch {
        if (measurement.isNotBlank() && isMeasurementValid) {
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

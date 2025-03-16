package com.clp3z.mysugrtest.features.measurements.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.common.toUnitValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementsViewModel @Inject constructor(
    private val getGlucoseMeasurementsUseCase: GetGlucoseMeasurementsUseCase
) : ViewModel() {

    data class ViewState(
        val measurements: List<GlucoseMeasurement> = emptyList(),
        val selectedUnit: GlucoseUnit? = null
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private var originalMeasurements: List<GlucoseMeasurement> = emptyList()

    fun initialize(selectedUnit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = selectedUnit) }
        getGlucoseMeasurementsUseCase().collect { result ->
            result.fold(
                ifLeft = {},
                ifRight = { measurements ->
                    originalMeasurements = measurements
                    val convertedMeasurements = convertMeasurements(measurements, selectedUnit)
                    _viewState.update { it.copy(measurements = convertedMeasurements) }
                }
            )
        }
    }

    private fun convertMeasurements(
        measurements: List<GlucoseMeasurement>,
        selectedUnit: GlucoseUnit
    ): List<GlucoseMeasurement> {
        return measurements.map { measurement ->
            if (measurement.unit != selectedUnit) {
                measurement.copy(
                    value = measurement.value.toUnitValue(unit = selectedUnit),
                    unit = selectedUnit
                )
            } else {
                measurement
            }
        }
    }

    fun onUnitSelected(unit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = unit) }
        val convertedMeasurements = convertMeasurements(originalMeasurements, unit)
        _viewState.update { it.copy(measurements = convertedMeasurements) }
    }
}

package com.clp3z.mysugrtest.features.measurements.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.common.convertMeasurements
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
        val selectedUnit: GlucoseUnit = GlucoseUnit.MG_DL
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private var originalMeasurements: List<GlucoseMeasurement> = emptyList()
    private val selectedUnit get() = viewState.value.selectedUnit

    fun initialize(selectedUnit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = selectedUnit) }
        collectMeasurements()
    }

    private suspend fun collectMeasurements() {
        getGlucoseMeasurementsUseCase().collect { result ->
            result.fold(
                ifLeft = {},
                ifRight = { measurements ->
                    originalMeasurements = measurements
                    val convertedMeasurements = convertMeasurements(
                        measurements = measurements,
                        selectedUnit = selectedUnit
                    )
                    _viewState.update { it.copy(measurements = convertedMeasurements) }
                }
            )
        }
    }

    fun onUnitSelected(unit: GlucoseUnit) = viewModelScope.launch {
        _viewState.update { it.copy(selectedUnit = unit) }
        val convertedMeasurements = convertMeasurements(originalMeasurements, unit)
        _viewState.update { it.copy(measurements = convertedMeasurements) }
    }
}

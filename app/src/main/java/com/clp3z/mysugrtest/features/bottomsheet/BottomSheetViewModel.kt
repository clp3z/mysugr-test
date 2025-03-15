package com.clp3z.mysugrtest.features.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.domain.AddGlucoseMeasurementUseCase
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor(
    private val getGlucoseMeasurementsUseCase: GetGlucoseMeasurementsUseCase,
    private val addGlucoseMeasurementUseCase: AddGlucoseMeasurementUseCase
) : ViewModel() {

    data class ViewState(
        val average: Float? = null,
        val selectedUnit: GlucoseUnit = GlucoseUnit.MG_DL,
        val measurement: String? = null,
        val isMeasurementValid: Boolean = true
    )

    private val selectedUnit get () = _viewState.value.selectedUnit
    private val measurement get () = _viewState.value.measurement
    private val average get () = _viewState.value.average

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun initialize() = viewModelScope.launch {
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

    fun onUnitSelected(unit: GlucoseUnit) = viewModelScope.launch {
        val measurement = measurement ?: return@launch
        if (unit != selectedUnit) {
            if (measurement.isMeasurementValid()) {
                val (newMeasurement, newAverage) = when (unit) {
                    GlucoseUnit.MG_DL -> measurement.toFloat().toMgDl() to average?.toMgDl()
                    GlucoseUnit.MMOL_L -> measurement.toFloat().toMmolL() to average?.toMmolL()
                    else -> 0f to 0f
                }
                _viewState.update {
                    it.copy(
                        selectedUnit = unit,
                        measurement = newMeasurement.toString(),
                        average = newAverage
                    )
                }
            } else {
                _viewState.update { it.copy(isMeasurementValid = false) }
            }
        }
    }

    fun onSaveMeasurementClick() = viewModelScope.launch {
        val measurement = measurement ?: return@launch
        if (measurement.isMeasurementValid()) {
            addGlucoseMeasurementUseCase(
                measurement = GlucoseMeasurement(
                    value = measurement.toFloat(),
                    unit = selectedUnit
                )
            )
            _viewState.update { it.copy(measurement = null) }
        } else {
            _viewState.update { it.copy(isMeasurementValid = false) }
        }
    }
}
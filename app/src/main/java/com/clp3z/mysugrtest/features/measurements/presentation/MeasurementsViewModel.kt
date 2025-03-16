package com.clp3z.mysugrtest.features.measurements.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
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

    data class ViewState(val measurements: List<GlucoseMeasurement> = emptyList())

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun initialize() = viewModelScope.launch {
        getGlucoseMeasurementsUseCase().collect { result ->
            result.fold(
                ifLeft = {},
                ifRight = { measurements ->
                    _viewState.update { it.copy(measurements = measurements) }
                }
            )
        }
    }
}

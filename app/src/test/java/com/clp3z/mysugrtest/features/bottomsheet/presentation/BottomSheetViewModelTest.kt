package com.clp3z.mysugrtest.features.bottomsheet.presentation

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.clp3z.mysugrtest.domain.AddGlucoseMeasurementUseCase
import com.clp3z.mysugrtest.domain.GetGlucoseMeasurementsUseCase
import com.clp3z.mysugrtest.entity.Error
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.features.common.BottomSheetViewModel
import com.clp3z.mysugrtest.framework.ui.input.InputFieldData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BottomSheetViewModelTest {

    private val getGlucoseMeasurementsUseCase = mockk<GetGlucoseMeasurementsUseCase>()
    private val addGlucoseMeasurementUseCase = mockk<AddGlucoseMeasurementUseCase>()
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: BottomSheetViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { getGlucoseMeasurementsUseCase() } returns flow {
            emit(emptyList<GlucoseMeasurement>().right())
        }
        coEvery { addGlucoseMeasurementUseCase(any()) } returns Unit.right()

        viewModel = BottomSheetViewModel(
            getGlucoseMeasurementsUseCase,
            addGlucoseMeasurementUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initializing with unit should set the selected unit`() = runTest {
        // When
        viewModel.initialize(GlucoseUnit.MMOL_L)

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(GlucoseUnit.MMOL_L, state.selectedUnit)
        }
    }

    @Test
    fun `selecting unit should update the unit in viewState`() = runTest {
        viewModel.viewState.test {

            // Given
            val initialState = awaitItem()
            assertEquals(GlucoseUnit.MG_DL, initialState.selectedUnit)

            // When
            viewModel.onUnitSelected(GlucoseUnit.MMOL_L)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val updatedState = awaitItem()
            println("Updated state: $updatedState")
            assertEquals(GlucoseUnit.MMOL_L, updatedState.selectedUnit)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changing unit should convert average to new unit`() = runTest {
        // Given
        val measurements = listOf(
            GlucoseMeasurement(value = 90f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 180f, unit = GlucoseUnit.MG_DL)
        )
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(measurements.right()) }

        viewModel.initialize(GlucoseUnit.MG_DL)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onUnitSelected(GlucoseUnit.MMOL_L)

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(GlucoseUnit.MMOL_L, state.selectedUnit)
            assertEquals(7.5f, state.average)
        }
    }

    @Test
    fun `changing unit should convert measurement value to new unit`() = runTest {
        // Given
        viewModel.initialize(GlucoseUnit.MG_DL)

        val measurementValue = "180"
        val inputFieldDataFlow = MutableStateFlow(InputFieldData(measurementValue))
        viewModel.collectMeasurementInputField(inputFieldDataFlow)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.viewState.test {
            val initialState = awaitItem()
            assertEquals(GlucoseUnit.MG_DL, initialState.selectedUnit)
            assertEquals(measurementValue, initialState.measurement)
        }

        // When
        viewModel.onUnitSelected(GlucoseUnit.MMOL_L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.viewState.test {
            val updatedState = awaitItem()
            assertEquals(GlucoseUnit.MMOL_L, updatedState.selectedUnit)
            assertEquals("10.0", updatedState.measurement)
        }
    }

    @Test
    fun `changing unit should convert all measurements to new unit`() = runTest {
        // Given
        val originalMeasurements = listOf(
            GlucoseMeasurement(value = 90f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 180f, unit = GlucoseUnit.MG_DL)
        )
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(originalMeasurements.right()) }

        viewModel.initialize(GlucoseUnit.MG_DL)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onUnitSelected(GlucoseUnit.MMOL_L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(GlucoseUnit.MMOL_L, state.selectedUnit)

            // Check that all measurements were converted
            assertEquals(2, state.measurements.size)
            assertEquals(5.0f, state.measurements[0].value, 0.1f)
            assertEquals(10.0f, state.measurements[1].value, 0.1f)
            assertEquals(GlucoseUnit.MMOL_L, state.measurements[0].unit)
            assertEquals(GlucoseUnit.MMOL_L, state.measurements[1].unit)
        }
    }

    @Test
    fun `changing unit with mixed unit measurements should convert all to target unit`() = runTest {
        // Given
        val mixedMeasurements = listOf(
            GlucoseMeasurement(value = 90f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 10f, unit = GlucoseUnit.MMOL_L)
        )
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(mixedMeasurements.right()) }

        viewModel.initialize(GlucoseUnit.MG_DL)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.viewState.test {
            val initialState = awaitItem()
            assertEquals(GlucoseUnit.MG_DL, initialState.selectedUnit)
            assertEquals(2, initialState.measurements.size)
            assertEquals(90f, initialState.measurements[0].value)
            assertEquals(180f, initialState.measurements[1].value, 0.1f) // 10 mmol/L ≈ 180 mg/dL
            assertEquals(GlucoseUnit.MG_DL, initialState.measurements[0].unit)
            assertEquals(GlucoseUnit.MG_DL, initialState.measurements[1].unit)
        }

        // When
        viewModel.onUnitSelected(GlucoseUnit.MMOL_L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(GlucoseUnit.MMOL_L, state.selectedUnit)

            // Check that all measurements were converted to mmol/L
            assertEquals(2, state.measurements.size)
            assertEquals(5.0f, state.measurements[0].value, 0.1f) // 90 mg/dL ≈ 5.0 mmol/L
            assertEquals(10.0f, state.measurements[1].value, 0.1f) // Original value
            assertEquals(GlucoseUnit.MMOL_L, state.measurements[0].unit)
            assertEquals(GlucoseUnit.MMOL_L, state.measurements[1].unit)
        }
    }

    @Test
    fun `collecting measurement input field should update measurement in viewState`() = runTest {
        // Given
        val inputField = MutableStateFlow(InputFieldData(""))

        // When
        viewModel.collectMeasurementInputField(inputField)
        inputField.value = InputFieldData("120")

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals("120", state.measurement)
        }
    }

    @Test
    fun `entering invalid measurement should update viewState isMeasurementValid to false`() = runTest {
        // Given
        viewModel.initialize(GlucoseUnit.MG_DL)

        // When
        val inputField = MutableStateFlow(InputFieldData("-10"))
        viewModel.collectMeasurementInputField(inputField)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals("-10", state.measurement)
            assertFalse(state.isMeasurementValid)
        }
    }

    @Test
    fun `entering valid measurement after invalid one should update viewState isMeasurementValid to true`() = runTest {
        // Given
        viewModel.initialize(GlucoseUnit.MG_DL)
        val invalidInputField = MutableStateFlow(InputFieldData("-10"))
        viewModel.collectMeasurementInputField(invalidInputField)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.viewState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isMeasurementValid)
        }

        // When
        val validInputField = MutableStateFlow(InputFieldData("120"))
        viewModel.collectMeasurementInputField(validInputField)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.viewState.test {
            val updatedState = awaitItem()
            assertEquals("120", updatedState.measurement)
            assertTrue(updatedState.isMeasurementValid)
        }
    }

    @Test
    fun `clicking save with valid input should save measurement`() = runTest {
        // Given
        viewModel.initialize(GlucoseUnit.MG_DL)
        val inputField = MutableStateFlow(InputFieldData("120"))
        viewModel.collectMeasurementInputField(inputField)
        testDispatcher.scheduler.advanceUntilIdle()

        val measurementSlot = slot<GlucoseMeasurement>()
        coEvery { addGlucoseMeasurementUseCase(capture(measurementSlot)) } returns Unit.right()

        // When
        viewModel.onSaveMeasurementClick()

        // Then
        coVerify { addGlucoseMeasurementUseCase(any()) }
        assertEquals(120f, measurementSlot.captured.value)
        assertEquals(GlucoseUnit.MG_DL, measurementSlot.captured.unit)
    }

    @Test
    fun `clicking save with successful save should clear input`() = runTest {
        // Given
        viewModel.initialize(GlucoseUnit.MG_DL)
        val inputField = MutableStateFlow(InputFieldData("120"))
        viewModel.collectMeasurementInputField(inputField)
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { addGlucoseMeasurementUseCase(any()) } returns Unit.right()

        // When
        viewModel.onSaveMeasurementClick()

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals("", state.measurement)
        }
    }

    @Test
    fun `clicking save with invalid input should not save measurement`() = runTest {
        // Given
        val inputField = MutableStateFlow(InputFieldData("-10"))
        viewModel.collectMeasurementInputField(inputField)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSaveMeasurementClick()

        // Then
        coVerify(exactly = 0) { addGlucoseMeasurementUseCase(any()) }
    }

    @Test
    fun `clicking save with empty input should not save measurement`() = runTest {
        // Given
        val inputField = MutableStateFlow(InputFieldData(""))
        viewModel.collectMeasurementInputField(inputField)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSaveMeasurementClick()

        // Then
        coVerify(exactly = 0) { addGlucoseMeasurementUseCase(any()) }
    }

    @Test
    fun `initializing with measurements should calculate correct average`() = runTest {
        // Given
        val measurements = listOf(
            GlucoseMeasurement(value = 60f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 90f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 120f, unit = GlucoseUnit.MG_DL)
        )
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(measurements.right()) }

        // When
        viewModel.initialize(GlucoseUnit.MG_DL)

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(90f, state.average)
        }
    }

    @Test
    fun `initializing with measurements should update viewState with measurements`() = runTest {
        // Given
        val measurements = listOf(
            GlucoseMeasurement(value = 60f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 90f, unit = GlucoseUnit.MG_DL),
            GlucoseMeasurement(value = 120f, unit = GlucoseUnit.MG_DL)
        )
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(measurements.right()) }

        // When
        viewModel.initialize(GlucoseUnit.MG_DL)

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertEquals(measurements, state.measurements)
        }
    }

    @Test
    fun `initializing with no measurements should set average to null`() = runTest {
        // Given
        every { getGlucoseMeasurementsUseCase() } returns flow {
            emit(emptyList<GlucoseMeasurement>().right())
        }

        // When
        viewModel.initialize(GlucoseUnit.MG_DL)

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            assertNull(state.average)
        }
    }

    @Test
    fun `initializing with error response should emit error through errorEvent`() = runTest {
        // Given
        val expectedError = Error.Database("Test error")
        every { getGlucoseMeasurementsUseCase() } returns flow { emit(expectedError.left()) }

        viewModel.errorEvent.test {

            // When
            viewModel.initialize(GlucoseUnit.MG_DL)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val emittedError = awaitItem()
            assertEquals(expectedError, emittedError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `exception in getMeasurements should emit unknown error through errorEvent`() = runTest {
        // Given
        val testException = RuntimeException("Test exception")
        every { getGlucoseMeasurementsUseCase() } returns flow { throw testException }

        viewModel.errorEvent.test {

            // When
            viewModel.initialize(GlucoseUnit.MG_DL)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val emittedError = awaitItem()
            assertTrue(emittedError is Error.Unknown)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

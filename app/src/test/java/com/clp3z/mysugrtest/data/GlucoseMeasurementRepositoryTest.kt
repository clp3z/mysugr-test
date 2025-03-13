package com.clp3z.mysugrtest.data

import app.cash.turbine.test
import com.clp3z.mysugrtest.entity.Error.*
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.framework.persistence.datasources.LocalGlucoseMeasurementDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class GlucoseMeasurementRepositoryTest {
    private lateinit var localDataSource: LocalGlucoseMeasurementDataSource
    private lateinit var repository: GlucoseMeasurementRepository
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    // Test data
    private val testMeasurement = GlucoseMeasurement(
        id = 0,
        unit = GlucoseUnit.MMOL_L,
        value = 5.5f
    )
    private val testMeasurements = listOf(
        testMeasurement.copy(id = 1),
        testMeasurement.copy(id = 2)
    )

    @Before
    fun setup() {
        localDataSource = mockk()
        repository = GlucoseMeasurementRepository(localDataSource)
    }

    @Test
    fun `maps successful retrieval from local data source to Either Right`() = testScope.runTest {
        // Given
        every { localDataSource.getGlucoseMeasurements() } returns flowOf(testMeasurements)

        // When & Then
        repository.getGlucoseMeasurements().test(timeout = 3.seconds) {
            val result = awaitItem()
            assertTrue(result.isRight())
            assertEquals(testMeasurements, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `returns mapped Database Error when retrieving measurements from local data source fails`() = testScope.runTest {
        // Given
        val message = "Test database error"
        val testException = RuntimeException(message)
        every { localDataSource.getGlucoseMeasurements() } returns flow { throw testException }

        // When & Then
        repository.getGlucoseMeasurements().test(timeout = 3.seconds) {
            val result = awaitItem()
            assertTrue(result.isLeft())

            val error = result.leftOrNull()
            assertTrue(error is Database)
            assertEquals(message, (error as Database).message)

            awaitComplete()
        }
    }

    @Test
    fun `maps successful insertion via local data source to Either Right`() = testScope.runTest {
        // Given
        coEvery { localDataSource.insertGlucoseMeasurement(any()) } returns Unit

        // When
        val testMeasurement = testMeasurement.copy(id = 1)
        val result = repository.insertMeasurement(testMeasurement)

        // Then
        assertTrue(result.isRight())
        coVerify { localDataSource.insertGlucoseMeasurement(testMeasurement) }
    }

    @Test
    fun `returns mapped Database Error when inserting measurement via local data source fails`() = testScope.runTest {
        // Given
        val message = "Test insertion error"
        val testException = RuntimeException(message)
        coEvery { localDataSource.insertGlucoseMeasurement(any()) } throws testException

        // When
        val testMeasurement = testMeasurement.copy(id = 1)
        val result = repository.insertMeasurement(testMeasurement)

        // Then
        assertTrue(result.isLeft())

        val error = result.leftOrNull()
        assertTrue(error is Database)
        assertEquals(message, (error as Database).message)
    }

    @Test
    fun `emits new measurements when local data source flow is updates`() = testScope.runTest {
        // Given
        val measurementsFlow = MutableSharedFlow<List<GlucoseMeasurement>>(replay = 1)
        every { localDataSource.getGlucoseMeasurements() } returns measurementsFlow

        val testMeasurement1 = testMeasurement.copy(id = 1)
        measurementsFlow.emit(listOf(testMeasurement1))

        // When & Then
        repository.getGlucoseMeasurements().test {
            // First emission
            val result1 = awaitItem()
            assertTrue(result1.isRight())
            assertEquals(listOf(testMeasurement1), result1.getOrNull())

            // Emit a new value
            val testMeasurement2 = testMeasurement.copy(id = 2)
            measurementsFlow.emit(listOf(testMeasurement1, testMeasurement2))

            // Second emission
            val result2 = awaitItem()
            assertTrue(result2.isRight())
            assertEquals(listOf(testMeasurement1, testMeasurement2), result2.getOrNull())

            // No more emissions expected at this point
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }
}

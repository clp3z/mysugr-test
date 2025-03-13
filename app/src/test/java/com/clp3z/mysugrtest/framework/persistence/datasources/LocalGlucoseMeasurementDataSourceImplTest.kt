package com.clp3z.mysugrtest.framework.persistence.datasources

import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit
import com.clp3z.mysugrtest.framework.persistence.dao.GlucoseMeasurementDAO
import com.clp3z.mysugrtest.framework.persistence.model.LocalGlucoseMeasurement
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocalGlucoseMeasurementDataSourceImplTest {

    private val glucoseMeasurementDAO = mockk<GlucoseMeasurementDAO>()
    private lateinit var dataSource: LocalGlucoseMeasurementDataSourceImpl

    private val testLocalMeasurement = LocalGlucoseMeasurement(
        id = 0,
        unit = GlucoseUnit.MMOL_L,
        value = 5.5f
    )

    private val testMeasurement = GlucoseMeasurement(
        id = 0,
        unit = GlucoseUnit.MMOL_L,
        value = 5.5f
    )

    @Before
    fun setup() {
        dataSource = LocalGlucoseMeasurementDataSourceImpl(glucoseMeasurementDAO)
    }

    @Test
    fun `maps to domain model and retrieves glucose measurements via DAO`() = runTest {
        // Given
        val localMeasurements = listOf(
            testLocalMeasurement.copy(id = 1),
            testLocalMeasurement.copy(id = 2)
        )
        val expectedMeasurements = listOf(
            testMeasurement.copy(id = 1),
            testMeasurement.copy(id = 2)
        )

        every { glucoseMeasurementDAO.getGlucoseMeasurements() } returns flowOf(localMeasurements)

        // When
        val result = dataSource.getGlucoseMeasurements().first()

        // Then
        verify { glucoseMeasurementDAO.getGlucoseMeasurements() }
        assertEquals(expectedMeasurements, result)
    }

    @Test
    fun `maps to database model and inserts glucose measurement via DAO`() = runTest {
        // Given
        val measurement = testMeasurement.copy(id = 1)
        val expectedLocalMeasurement = testLocalMeasurement.copy(id = 1)

        coEvery {
            glucoseMeasurementDAO.insertGlucoseMeasurement(expectedLocalMeasurement)
        } just runs

        // When
        dataSource.insertGlucoseMeasurement(measurement)

        // Then
        coVerify {
            glucoseMeasurementDAO.insertGlucoseMeasurement(expectedLocalMeasurement)
        }
    }
}

package com.clp3z.mysugrtest.framework.persistence.datasources

import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import kotlinx.coroutines.flow.Flow

interface LocalGlucoseMeasurementDataSource {

    fun getGlucoseMeasurements(): Flow<List<GlucoseMeasurement>>

    suspend fun insertGlucoseMeasurement(measurement: GlucoseMeasurement)
}
package com.clp3z.mysugrtest.framework.persistence.datasources

import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.framework.persistence.dao.GlucoseMeasurementDAO
import com.clp3z.mysugrtest.framework.persistence.toGlucoseMeasurement
import com.clp3z.mysugrtest.framework.persistence.toGlucoseMeasurements
import com.clp3z.mysugrtest.framework.persistence.toLocalGlucoseMeasurement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalGlucoseMeasurementDataSourceImpl @Inject constructor(
    private val glucoseMeasurementDAO: GlucoseMeasurementDAO
): LocalGlucoseMeasurementDataSource{

    override fun getGlucoseMeasurements(): Flow<List<GlucoseMeasurement>> =
        glucoseMeasurementDAO
            .getGlucoseMeasurements()
            .map { it.toGlucoseMeasurements() }

    override suspend fun insertGlucoseMeasurement(measurement: GlucoseMeasurement) =
        glucoseMeasurementDAO.insertGlucoseMeasurement(measurement.toLocalGlucoseMeasurement())
}

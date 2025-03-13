package com.clp3z.mysugrtest.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.clp3z.mysugrtest.entity.Error
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.framework.persistence.datasources.LocalGlucoseMeasurementDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GlucoseMeasurementRepository @Inject constructor(
    private val localGlucoseMeasurementDataSource: LocalGlucoseMeasurementDataSource
) {

    fun getGlucoseMeasurements(): Flow<Either<Error, List<GlucoseMeasurement>>> =
        localGlucoseMeasurementDataSource
            .getGlucoseMeasurements()
            .map { it.rightWithError() }
            .catch { emit(it.toDatabaseError().leftWithError()) }

    suspend fun insertMeasurement(measurement: GlucoseMeasurement): Either<Error, Unit> = try {
        localGlucoseMeasurementDataSource.insertGlucoseMeasurement(measurement)
        Unit.right()
    } catch (exception: Exception) {
        exception.toDatabaseError().left()
    }
}

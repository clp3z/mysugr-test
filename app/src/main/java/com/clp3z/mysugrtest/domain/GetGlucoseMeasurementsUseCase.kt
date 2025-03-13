package com.clp3z.mysugrtest.domain

import arrow.core.Either
import com.clp3z.mysugrtest.data.GlucoseMeasurementRepository
import com.clp3z.mysugrtest.entity.Error
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGlucoseMeasurementsUseCase @Inject constructor(
    private val repository: GlucoseMeasurementRepository
) {

    operator fun invoke(): Flow<Either<Error, List<GlucoseMeasurement>>> =
        repository.getGlucoseMeasurements()
}

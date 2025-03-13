package com.clp3z.mysugrtest.domain

import arrow.core.Either
import com.clp3z.mysugrtest.data.GlucoseMeasurementRepository
import com.clp3z.mysugrtest.entity.Error
import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import javax.inject.Inject

class AddGlucoseMeasurementUseCase @Inject constructor(
    private val repository: GlucoseMeasurementRepository
) {

    suspend operator fun invoke(measurement: GlucoseMeasurement): Either<Error, Unit> =
        repository.addGlucoseMeasurement(measurement)
}

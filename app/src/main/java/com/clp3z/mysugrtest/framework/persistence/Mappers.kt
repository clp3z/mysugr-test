package com.clp3z.mysugrtest.framework.persistence

import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.framework.persistence.model.LocalGlucoseMeasurement

fun LocalGlucoseMeasurement.toGlucoseMeasurement() = GlucoseMeasurement(
    id = id,
    unit = unit,
    value = value
)

fun GlucoseMeasurement.toLocalGlucoseMeasurement() = LocalGlucoseMeasurement(
    id = id,
    unit = unit,
    value = value
)

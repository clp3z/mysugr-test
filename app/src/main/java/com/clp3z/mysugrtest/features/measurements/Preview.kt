package com.clp3z.mysugrtest.features.measurements

import com.clp3z.mysugrtest.entity.GlucoseMeasurement
import com.clp3z.mysugrtest.entity.GlucoseUnit

val previewMeasurement = GlucoseMeasurement(
    id = 1,
    value = 180f,
    unit = GlucoseUnit.MG_DL
)

val previewMeasurements = mutableListOf<GlucoseMeasurement>().apply {
    repeat(16) {
        add(
            previewMeasurement.copy(
                id = (it + 1).toLong(),
                unit = if (it % 2 == 0) GlucoseUnit.MG_DL else GlucoseUnit.MMOL_L
            )
        )
    }
}
